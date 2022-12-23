import os
import pytest
from export_openvino import export_tf_as_openvino

CUR_DIR = os.path.join(os.path.dirname(__file__))

def test_export_tf():
    model_dir = f"{CUR_DIR}/test_models/tf/checkpoints/best.ckpt"
    output_dir = f"{CUR_DIR}/test_models/openvino"
    export_tf_as_openvino(model_dir, output_dir)

if __name__ == '__main__':
    test_export_tf()