#!/usr/bin/env bash

set -e

eval "$(conda shell.bash hook)"
conda activate openbot
adev runserver openbot/server
