#!/bin/bash

# Activate virtual environment
source venv/bin/activate

# Run the test runner with any passed arguments
python test_runner.py "$@"

# Deactivate virtual environment
deactivate 