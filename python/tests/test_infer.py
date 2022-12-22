import pytest
from infer import InferNetwork
import os

CUR_DIR = os.path.join(os.path.dirname(__file__))
DATASET_PATH = f"{CUR_DIR}/test_data/logs1/data/"
def create_network(backend):

    policy = InferNetwork(
                policy_path=f"{CUR_DIR}/test_models/{backend}",
                 dataset_path=DATASET_PATH,
                 log_path=None,
                 run_mode="debug",
                 inference_backend=backend)
    return policy

def test_debug():
    # create_network("tf").run()
    # create_network("tflite").run()
    create_network("openvino").run()

def replace_path(path_to_openbot):
    file_path = f"{DATASET_PATH}/sensor_data/matched_frame_ctrl_cmd_processed_template.txt"
    with open(file_path, 'r') as file :
        filedata = file.read()

    filedata = filedata.replace('/path/to/OpenBot', path_to_openbot)

    file_path = f"{DATASET_PATH}/sensor_data/matched_frame_ctrl_cmd_processed.txt"
    with open(file_path, 'w') as file:
        file.write(filedata)

if __name__ == "__main__":
    print("The test data in logs1 is generated using the associate_frames.py "
          +"script in OpenBot.policy.openbot, where the path to the images is "
          +"hardcoded in logs1/data/sensor_data/matched_frame_ctrl_cmd_processed.txt."
          +" Thus, replacing the /path/to/OpenBot with the actual path to the OpenBot repository.")
    path_to_openbot = "/home/yuankai/code/github/OpenBot"
    assert len(path_to_openbot) > 0, "Please enter the path_to_openbot in test_infer.py"
    replace_path(path_to_openbot)

    test_debug()