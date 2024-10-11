

import json
import re
from pathlib import Path


def load_most_recent_simulation_json(directory_path: str):
    # Define the pattern for matching the file names
    pattern = re.compile(r"ex1_results_\d{8}_\d{6}\.json")

    # Get a list of all files in the directory that match the pattern
    files = [f for f in Path(directory_path).iterdir() if pattern.match(f.name)]

    if not files:
        print("No simulation files found.")
        return None

    # Sort files based on the timestamp in the filename
    most_recent_file = max(files, key=lambda f: f.stem.split('_')[1:])

    # Open and return the JSON data from the file
    with most_recent_file.open('r') as file:
        print(f'Opening file {most_recent_file.name}')
        return json.load(file)


def load_most_recent_simulation_json_ex2(directory_path: str):
    # Define the pattern for matching the file names
    pattern = re.compile(r"ex2_results_\d{8}_\d{6}\.json")

    # Get a list of all files in the directory that match the pattern
    files = [f for f in Path(directory_path).iterdir() if pattern.match(f.name)]

    if not files:
        print("No simulation files found.")
        return None

    # Sort files based on the timestamp in the filename
    most_recent_file = max(files, key=lambda f: f.stem.split('_')[1:])

    # Open and return the JSON data from the file
    with most_recent_file.open('r') as file:
        print(f'Opening file {most_recent_file.name}')
        loaded_file = json.load(file)
        print(f'Loaded file {most_recent_file.name}')
        return loaded_file
