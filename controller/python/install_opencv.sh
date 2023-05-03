#!/bin/bash
# Reference: https://discuss.bluerobotics.com/t/opencv-python-with-gstreamer-backend/8842
BASEDIR="$( cd -- "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"
echo "Script location: ${BASEDIR}"
cd $BASEDIR/../../..
echo "Cloning opencv-python at $PWD"
git clone --recursive https://github.com/skvark/opencv-python.git
echo "Building and installing opencv_python with GStreamer support..."
echo "This can take from 5 mins to > 2 hrs depending on your computer hardware."
cd opencv-python
export CMAKE_ARGS="-DWITH_GSTREAMER=ON"
pip install --upgrade pip wheel
pip wheel . --verbose
pip install opencv_python*.whl
# note, wheel may be generated in dist/ directory, so check there if the install fails
cd $BASEDIR