"""
Created by Marcel Santos - Intel Intelligent Systems Lab - 2021
This script implements a tf-records generator for more robust data loading.
Specifically, the user specifies to path of the collected dataset and the path for storing the output tfrecord 
file and this script will create a tfrecord with the dataset ready to be used for training a drive policy.
"""

import argparse
import os
import json
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


def load_labels(data_dir, datasets, policy="autopilot"):
    """Returns a dictionary of matched images path[string] and actions tuple, namely (left[int], right[int], cmd[int]) for autopilot policy and (left[int], right[int], dist[float],sinYaw[float],cosYaw[float]) for point_goal_nav policy."""

    if policy == "autopilot":
        processed_frames_file_name = "matched_frame_ctrl_cmd_processed.txt"
    elif policy == "point_goal_nav":
        processed_frames_file_name = "matched_frame_ctrl_goal_processed.txt"
    else:
        raise Exception("Unknown policy")

    corpus = []
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
            ) as f_input:
                header = f_input.readline()  # discard header
                data = f_input.read()
                #print(data)
                lines = (
                    data.replace(",", " ")
                    .replace("\\", "/")
                    .replace("\r", "")
                    .replace("\t", " ")
                    .split("\n")
                )
                data = [
                    [v.strip() for v in line.split(" ") if v.strip() != ""]
                    for line in lines
                    if len(line) > 0 and line[0] != "#"
                ]
                # Tuples containing id: framepath and label: "left,right,cmd" for autopiot policy
                # and for "left,right,dist,sinYaw,cosYaw" point_goal_nav policy.
                data = [(line[1], line[2:]) for line in data if len(line) > 1]
                corpus.extend(data)
    return dict(corpus)

def load_rewards(data_dir, datasets, policy="autopilot"):
    if policy == "autopilot":
        processed_rewards_file_name = "matched_frame_reward_processed.txt"
    elif policy == "point_goal_nav":
        processed_rewards_file_name = "matched_frame_reward_processed.txt"
    else:
        raise Exception("Unknown policy")
    
    rewards = []
    for dataset in datasets:
            for folder in [
                f
                for f in os.listdir(os.path.join(data_dir, dataset))
                    if not f.startswith(".")
                ]:
                    rewards_file = os.path.join(
                        data_dir,
                        dataset,
                        folder,
                        "reward_data",
                        "matched_frame_reward_processed.txt",
                    )
                    print("reward_file", rewards_file)
                    with open(rewards_file) as f_input:
                        # discard header
                        header = f_input.readline()
                        data = f_input.read()
                        #print("data: ", data)
                        lines = (
                            data.replace(",", " ")
                            .replace("\\", "/")
                            .replace("\r", "")
                            .replace("\t", " ")
                            .split("\n")
                        )
                

                        data = [
                            [v.strip() for v in line.split(" ") if v.strip() != ""]
                            for line in lines
                            if len(line) > 0 and line[0] != "#"
                        ]
                        # Tuples containing id: framepath and respectively labels "left,right,cmd" for autopilot policy
                        # and labels "left,right,dist,sinYaw,cosYaw" point_goal_nav policy
                        data = [(line[1], line[2:]) for line in data if len(line) > 1]
                        rewards.extend(data)
    
    return dict(rewards)


def convert_dataset(
    data_dir,
    tfrecords_dir,
    tfrecords_name,
    redo_matching=True,
    remove_zeros=True,
    policy="autopilot",
):
    print(f"Reading dataset from {data_dir}")
    print(f"TFRecord will be saved at {tfrecords_dir}/{tfrecords_name}")

    if policy == "autopilot":
        processed_frames_file_name = "matched_frame_ctrl_cmd_processed.txt"
    elif policy == "point_goal_nav":
        processed_frames_file_name = "matched_frame_ctrl_goal_processed.txt"
    else:
        raise Exception("Unknown policy")

    # load the datasets avaible.
    datasets = [
        d for d in os.listdir(data_dir) if os.path.isdir(os.path.join(data_dir, d))
    ]
    print("Number of Datasets Available: ", len(datasets))

    # match frames.
    max_offset = 1e3  # 1ms
    frames = associate_frames.match_frame_ctrl_input(
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
    samples = load_labels(data_dir, datasets, policy)

    rewards = load_rewards(data_dir, datasets, policy)


    updated_samples = {}
    combined_data = {}

    for abs_image_path, ctrl_input in samples.items():
    # Specify the substring to look for
        substring = "dataset/"

    # Find the index of the substring in the absolute path
        index = abs_image_path.find(substring)

    # If the substring is found, update the key with "dataset/" and the following part
        if index != -1:
            relative_path = substring + abs_image_path[index + len(substring):]
            updated_samples[relative_path] = ctrl_input

            reward_info = rewards.get(relative_path)

        # If reward_info exists, update the combined_data dictionary
            if reward_info is not None:
                combined_data[relative_path] = ctrl_input + reward_info

# Update the original samples dictionary with the new one
    samples = updated_samples
    with tf.io.TFRecordWriter(tfrecords_dir + "/" + tfrecords_name) as writer:
        for image_path, combined_input in combined_data.items():
            image_path = "c:/Users/lilou/Documents/Openbot/OpenBot-master/policy/" + image_path
            try:
                
                image = tf.io.decode_jpeg(tf.io.read_file(image_path))
                if policy == "autopilot":
                    example = tfrecord_utils.create_example_autopilot(
                        image, image_path, combined_input
                   )
                elif policy == "point_goal_nav":
                    example = tfrecord_utils.create_example_point_goal_nav(
                        image, image_path, combined_input
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

    convert_dataset(data_dir, tfrecords_dir, tfrecords_name)
