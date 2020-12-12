#!/usr/bin/env bash

set -e

trap 'kill 0' SIGINT

(
    eval "$(conda shell.bash hook)"
    conda activate openbot
    adev runserver openbot/server
) &

sleep 1
cd frontend
yarn start

fg
