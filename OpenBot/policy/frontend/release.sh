#!/usr/bin/env bash

set -e

cd $(dirname $0)

rm -rf build dist openbot_frontend

yarn install
yarn build

eval "$(conda shell.bash hook)"
conda activate openbot

mv build openbot_frontend
python setup.py sdist
twine upload dist/*
