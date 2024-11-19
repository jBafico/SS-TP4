
import os
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


def load_most_recent_simulation_json_ex2(timestamp: str= None):
    
    # Base directory where the simulation files are stored
    base_dir = '../outputs/ex2_results'

    # Determine the directory based on the timestamp or find the newest one
    if timestamp:
        target_dir = os.path.join(base_dir, timestamp)
    else:
        # Get all timestamped directories in the outputs folder
        directories = [
            d for d in os.listdir(base_dir) if os.path.isdir(os.path.join(base_dir, d))
        ]
        # Sort directories by descending timestamp to get the latest one
        directories.sort(reverse=True)
        target_dir = os.path.join(base_dir, directories[0]) if directories else None

    if not target_dir:
        raise FileNotFoundError("No directories found with the given or latest timestamp.")

    return target_dir
