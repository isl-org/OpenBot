import os
import time

import numpy as np
from . import api
from .. import associate_frames, dataset_dir, models_dir, utils


def getPrediction(params):
    if not params["model"]:
        return []

    # Load the TFLite model and allocate tensors.
    interpreter = utils.load_tflite(
        models_dir, params["model"], "checkpoints", "best.tflite"
    )
    interpreter.allocate_tensors()

    # Get input and output tensors.
    input_details = interpreter.get_input_details()
    output_details = interpreter.get_output_details()
    """
    input_details [
        {'name': 'cmd_input', 'index': 66, 'shape': array([1, 1], dtype=int32),
                                                            'dtype': <class 'numpy.float32'>, 'quantization': (0.0, 0)},
        {'name': 'img_input', 'index': 67, 'shape': array([  1,  96, 256,   3], dtype=int32),
                                                            'dtype': <class 'numpy.float32'>, 'quantization': (0.0, 0)}]
    output_details [
        {'name': 'Identity', 'index': 0, 'shape': array([1, 2], dtype=int32),
        'dtype': <class 'numpy.float32'>, 'quantization': (0.0, 0)}]
    """

    real_path = dataset_dir + params["path"]
    frames = associate_frames.read_file_list(
        real_path + "/sensor_data/matched_frame_ctrl_cmd.txt"
    )
    result = np.empty((0, 2), dtype=int)
    cmd_input = np.array([[params["indicator"] or 0]], dtype=np.float32)
    start = params["start"]
    end = params["end"]
    keys = list(frames)[start:end]
    print("get prediction from frame " + str(start))
    for frame in keys:
        _, _, img, left, right, ind = frames[frame]
        path = f"{real_path}/images/{img}_crop.jpeg"
        # print(path, left, right)
        if params["indicator"] is None:
            cmd_input = np.array([[ind]], dtype=np.float32)

        img = utils.load_img(path)
        img_input = np.expand_dims(img, axis=0)

        interpreter.set_tensor(input_details[0]["index"], cmd_input)
        interpreter.set_tensor(input_details[1]["index"], img_input)
        interpreter.invoke()
        output = interpreter.get_tensor(output_details[0]["index"]) * 255
        result = np.concatenate((result, output.astype(int)))

    return api.encode(result)


if __name__ == "__main__":
    os.environ["TF_CPP_MIN_LOG_LEVEL"] = "2"

    params = dict(
        path="/train_data/openbot1/20201212_154310",
        model="my_openbot_cil_mobile_lr0.0001_bz16_bn",
        indicator="",
    )
    start_time = time.time()
    pred = getPrediction(params)
    print("Time:", time.time() - start_time)
    print(pred)
