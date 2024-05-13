import argparse
import os

import tensorflow as tf
from . import associate_frames
from . import tfrecord_utils

def get_parser():
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "--dataset-dir",
        "-i",
        type=str,
        required=True,
        help="Path of the collected dataset",
    )
    parser.add_argument(
        "--output-dir",
        "-o",
        type=str,
        required=True,
        help="Path for storing the output file",
    )
    parser.add_argument("--tfrecord-name", "-n", type=str, default="dataset.tfrec")
    return parser


def load_info(data_dir, datasets, policy="Reinforcement"):
    """Returns a dictionary of matched images path[string] and an info tuple, namely (int(forward), int(left), int(right), (int(reward)), (int(done))"""

    if policy == "Reinforcement":
        processed_frames_file_name = "matched_frame_info_processed.txt"
    else:
        raise Exception("Unknown policy")

    corpus = {}
    for dataset in datasets:
        dataset_folders = [
            f
            for f in os.listdir(os.path.join(data_dir, dataset))
            if not f.startswith(".")
        ]
        for folder in dataset_folders:
            sensor_data_dir = os.path.join(data_dir, dataset, folder, "sensor_data")
            with open(
                os.path.join(sensor_data_dir, processed_frames_file_name)
            ) as file:
                file.readline()  # Skip the first line
                for line in file:
                    line = line.replace("[", "").replace("]", "")
                    parts = line.strip().split(',')
                    if len(parts) < 7:
                        print("Error: Insufficient data in line:", line)
                        continue
                    image_path = parts[1]
                    forward = int(parts[2])
                    left = int(parts[3])
                    right = int(parts[4])
                    rewards = int(parts[5])
                    done = int(parts[6])
                    corpus[image_path] = (forward, left, right, rewards, done)
                    # print(f'Corpus for {image_path}: {corpus[image_path]}')
      
    return corpus


def convert_dataset_info(
    data_dir,
    tfrecords_dir,
    tfrecords_name,
    redo_matching=True,
    remove_zeros=True,
    policy="Reinforcement",
):
    print(f"Reading dataset from {data_dir}")
    print(f"TFRecord will be saved at {tfrecords_dir}/{tfrecords_name}")

    if policy == "Reinforcement":
        processed_frames_file_name = "infoLog_matched_frames.txt"
    else:
        raise Exception("Unknown policy")

    # load the datasets avaible.
    datasets = [
        d for d in os.listdir(data_dir) if os.path.isdir(os.path.join(data_dir, d))
    ]
    print("Number of Datasets Available: ", len(datasets))

    # match frames.
    max_offset = 1e3  # 1ms
    frames = associate_frames.match_frame_info_input(
        data_dir,
        datasets,
        max_offset,
        redo_matching=redo_matching,
        remove_zeros=remove_zeros,
        policy=policy,
    )

    # creating TFRecords output folder.
    if not os.path.exists(tfrecords_dir):
        os.makedirs(tfrecords_dir)

    # generate data in the TFRecord format.
    samples = load_info(data_dir, datasets, policy)


    with tf.io.TFRecordWriter(tfrecords_dir + "/" + tfrecords_name) as writer:
        for image_path, info_input in samples.items():
            try:
                image = tf.io.decode_jpeg(tf.io.read_file(image_path))
                image = preprocess_image(image)
                
                example = tfrecord_utils.create_example_reinforcement(
                    image, image_path, info_input
                )
                

                writer.write(example.SerializeToString())
            except:
                print(f"Oops! Image {image_path} cannot be found.")

    print("TFRecord file created successfully.")


if __name__ == "__main__":
    parser = get_parser()
    args = parser.parse_args()
    data_dir = args.dataset_dir
    tfrecords_dir = args.output_dir
    tfrecords_name = args.tfrecord_name

    convert_dataset_info(data_dir, tfrecords_dir, tfrecords_name)

def preprocess_image(image):
    # Remove the top 70% of black pixels
    height, width, _ = image.shape
    top_crop_height = int(height * 0.7)
    image = image[top_crop_height:, :, :]
    
    # Perform additional data augmentation or resizing here if needed
    
    return image