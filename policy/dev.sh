#!/usr/bin/env bash

set -e

trap 'kill 0' SIGINT

cd frontend
yarn start &
cd ..

eval "$(conda shell.bash hook)"
conda activate openbot
adev runserver openbot/server

fg
