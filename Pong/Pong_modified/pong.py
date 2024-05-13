""" Majority of this code was copied directly from Andrej Karpathy's gist:
https://gist.github.com/karpathy/a4166c7fe253700972fcbc77e4ea32c5 """

""" Trains an agent with (stochastic) Policy Gradients on Pong. Uses OpenAI Gym. """
import numpy as np
import pickle
import gym
import matplotlib.pyplot as plt
import os
import pandas as pd

# Initialize an empty DataFrame to store episode rewards
reward_data = pd.DataFrame(columns=['Episode', 'Reward'])




# hyperparameters to tune
H = 200 # number of hidden layer neurons
batch_size = 1 # used to perform a RMS prop param update every batch_size steps
learning_rate = 1e-3 # learning rate used in RMS prop
gamma = 0.99 # discount factor for reward
decay_rate = 0.99 # decay factor for RMSProp leaky sum of grad^2

# Config flags - video output and res
resume = False # resume training from previous checkpoint (from save.p  file)?
render = False # render video output?

# model initialization
D = 75 * 80 # input dimensionality: 75x80 grid
if resume:
  model = pickle.load(open('save.p', 'rb'))
else:
  model = {}
  model['W1'] = np.random.randn(D, H) / np.sqrt(D) # "Xavier" initialization - Shape will be H x D
  model['W2'] = np.random.randn(H, 2) / np.sqrt(H) # Shape will be H

grad_buffer = { k : np.zeros_like(v) for k,v in model.items() } # update buffers that add up gradients over a batch
rmsprop_cache = { k : np.zeros_like(v) for k,v in model.items() } # rmsprop memory

def sigmoid(x):
  return 1.0 / (1.0 + np.exp(-x)) # sigmoid "squashing" function to interval [0,1]

def activation(x):
   return np.tanh(x)

def softmax(x):
    """Compute softmax values for each sets of scores in x."""
    e_x = np.exp(x - np.max(x))  # Subtract max value for numerical stability
    return e_x / e_x.sum(axis=0)

def prepro(I):
  """ prepro 210x160x3 uint8 frame into 6000 (75x80) 1D float vector """
  I = np.array(I) 
  I = I[35:185] # crop - remove 35px from start & 25px from end of image in x, to reduce redundant parts of image (i.e. after ball passes paddle)
  I = I[::2,::2,0] # downsample by factor of 2.
  I[I == 144] = 0 # erase background (background type 1)
  I[I == 109] = 0 # erase background (background type 2)
  I[I != 0] = 1 # everything else (paddles, ball) just set to 1. this makes the image grayscale effectively
  return I.astype(float).ravel() # ravel flattens an array and collapses it into a column vector

def discount_rewards(r):
  """ take 1D float array of rewards and compute discounted reward """
  """ this function discounts from the action closest to the end of the completed game backwards
  so that the most recent action has a greater weight """
  discounted_r = np.zeros_like(r)
  running_add = 0
  for t in reversed(range(0, r.size)): # xrange is no longer supported in Python 3
    if r[t] != 0: running_add = 0 # reset the sum, since this was a game boundary (pong specific!)
    running_add = running_add * gamma + r[t]
    discounted_r[t] = running_add
  return discounted_r

def policy_forward(x):
  """This is a manual implementation of a forward prop"""
  h = np.dot(x.T, model['W1']) # (1 x D) @ (D x H) = (1 x H)
  h[h<0] = 0 # ReLU introduces non-linearity
  logp = np.dot(h, model['W2']) # (1 x H) @ (H x 3) = (1 x 3)  
  p = softmax(logp)  # squashes output to  between 0 & 1 range
  return p, h 

def policy_backward(eph, epx, epdlogp):
  """ backward pass. (eph is array of intermediate hidden states) """
  """ Manual implementation of a backward prop"""
  """ It takes an array of the hidden states that corresponds to all the images that were
  fed to the NN (for the entire episode, so a bunch of games) and their corresponding logp"""
  dW2 = np.dot(eph.T, epdlogp)
  dh = np.dot(epdlogp, model['W2'].T)
  dh[eph <= 0] = 0 # backpro prelu
  dW1 = np.dot(dh.T, epx)
  return {'W1':dW1.T, 'W2':dW2}

def choose_action(output, epsilon):
    """This function chooses an action based on the output and epsilon-greedy strategy."""
    is_random = False
    if np.random.uniform() < epsilon:
        is_random = True
        return np.random.choice([2, 3]), is_random  # Randomly choose one of the three actions
    else:
        action = np.random.choice(len(output), p=output)
        return action + 2, is_random
        
def create_fake_label(action):
    """Create a fake label based on the chosen action."""
    if action == 3:
        return [0, 1.0]  # For action 2
    elif action == 2:
        return [1.0, 0] # For action 3


env = gym.make("Pong-v0")
# env = gym.wrappers.Monitor(env, 'tmp/pong-base', force=True) # record the game as as an mp4 file
observation = env.reset()[0]
prev_x = None # used in computing the difference frame
xs,hs,dlogps,drs = [],[],[],[]
running_reward = None
reward_sum = 0
episode_number = 0
max_episodes = 15000
reward_plot = []
initial_epsilon = 0.15
epsilon_decay = 0.999

# Initialize current epsilon value
epsilon = initial_epsilon
while episode_number <= max_episodes:
  if render: env.render()

  # preprocess the observation, set input to network to be difference image
  cur_x = prepro(observation)
  # we take the difference in the pixel input, since this is more likely to account for interesting information
  # e.g. motion
  x = cur_x - prev_x if prev_x is not None else np.zeros(D)
  prev_x = cur_x

  # forward the policy network and sample an action from the returned probability
  aprob, h = policy_forward(x)
  # The following step is randomly choosing a number which is the basis of making an action decision
  # If the random number is less than the probability of UP output from our neural network given the image
  # then go down.  The randomness introduces 'exploration' of the Agent
  action, is_random = choose_action(aprob, epsilon) # roll the dice! 2 is UP, 3 is DOWN, 0 is stay the same

  # record various intermediates (needed later for backprop).
  # This code would have otherwise been handled by a NN library
  xs.append(x) # observation
  hs.append(h) # hidden state


  y = create_fake_label(action) # a "fake label" - this is the label that we're passing to the neural network
  # to fake labels for supervised learning. It's fake because it is generated algorithmically, and not based
  # on a ground truth, as is typically the case for Supervised learning
  if is_random == True:
    dlogps.append(y)
  else:
    dlogp = y - aprob
    dlogps.append(dlogp) # grad that encourages the action that was taken to be taken (see http://cs231n.github.io/neural-networks-2/#losses if confused)

  # step the environment and get new measurements
  observation, reward, done, info, _ = env.step(action)
  reward_sum += reward
  drs.append(reward) # record reward (has to be done after we call step() to get reward for previous action)

  if done: # an episode finished
    episode_number += 1
    epsilon *= epsilon_decay
    # stack together all inputs, hidden states, action gradients, and rewards for this episode
    epx = np.vstack(xs)
    eph = np.vstack(hs)
    epdlogp = np.vstack(dlogps)
    epr = np.vstack(drs)
    xs,hs,dlogps,drs = [],[],[],[] # reset array memory

    # compute the discounted reward backwards through time
    discounted_epr = discount_rewards(epr)
    # standardize the rewards to be unit normal (helps control the gradient estimator variance)
    discounted_epr -= np.mean(discounted_epr)
    discounted_epr /= np.std(discounted_epr)

    epdlogp *= discounted_epr # modulate the gradient with advantage (Policy Grad magic happens right here.)
    grad = policy_backward(eph, epx, epdlogp)
    for k in model: grad_buffer[k] += grad[k] # accumulate grad over batch

    # perform rmsprop parameter update every batch_size episodes
    if episode_number % batch_size == 0:
      for k,v in model.items():
        g = grad_buffer[k] # gradient
        rmsprop_cache[k] = decay_rate * rmsprop_cache[k] + (1 - decay_rate) * g**2
        model[k] += learning_rate * g / (np.sqrt(rmsprop_cache[k]) + 1e-5)
        grad_buffer[k] = np.zeros_like(v) # reset batch gradient buffer

    # boring book-keeping
    running_reward = reward_sum if running_reward is None else running_reward * 0.99 + reward_sum * 0.01
    print ('resetting env. episode reward total was %f. running mean: %f' % (reward_sum, running_reward))
    reward_plot.append(running_reward)
    reward_data = reward_data._append({'Episode': episode_number, 'Reward': running_reward}, ignore_index=True)
    if episode_number % 100 == 0: 
        pickle.dump(model, open('save.p', 'wb'))
        plt.plot(range(1, episode_number + 1), reward_plot)
        plt.xlabel('Episode')
        plt.ylabel('Reward Moving Average')
        plt.title('Reward Moving Average over Episodes')
        plt.savefig('running_rewards.png')
        plt.close()
        reward_data.to_csv('reward_data.csv', index=False)


    reward_sum = 0
    observation = env.reset()[0] # reset env
    prev_x = None

  if reward != 0: # Pong has either +1 or -1 reward exactly when game ends.
    print ('ep %d: game finished, reward: %f%s' % (episode_number, reward, '' if reward == -1 else ' !!!!!!!!'))

import cv2
import numpy as np

# Initialize the test environment
test_env = gym.make('Pong-v0', render_mode='rgb_array')
observation = test_env.reset()[0]
prev_x = None
frames = []

# Play the game using the final model
state = prepro(observation)
while True:
    # Render the environment
    frame = test_env.render()
    frames.append(frame)

    # Forward the policy network to get action probability
    x = prepro(observation) - prev_x if prev_x is not None else np.zeros(D)
    prev_x = prepro(observation)
    aprob, _ = policy_forward(x)

    # Choose action based on probability
    action, _ = choose_action(aprob, 0)

    # Take a step in the environment
    next_state, _, done, _, _ = test_env.step(action)
    observation = next_state

    if done:
        break

# Save the collected frames as a video
video_path = ('Final_model_video.avi')
frame_height, frame_width, _ = frames[0].shape
fps = 25

fourcc = cv2.VideoWriter_fourcc(*'XVID')
out = cv2.VideoWriter(video_path, fourcc, fps, (frame_width, frame_height))

for frame in frames:
    out.write(cv2.cvtColor(frame, cv2.COLOR_RGB2BGR))

out.release()