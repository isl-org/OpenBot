import os
import pytest
from generate_data_for_training import Logger

CUR_DIR = os.path.join(os.path.dirname(__file__))

def test_generate_data():
    print("Writing data")
    name = f"logs_generated"

    logger = Logger(name, logs_dir=f"{CUR_DIR}/test_data/")

    logger.write_files()
    print("done")
if __name__ == '__main__':
    test_generate_data()