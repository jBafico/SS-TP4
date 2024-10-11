from load_most_recent_json import load_most_recent_simulation_json
import json
from grapher_ex1_item2 import handle_ex1_item2_graph


def main():
    with open("item1config.json","r") as f:
        config = json.load(f)
    json_data = load_most_recent_simulation_json("../outputs")

    if config["item12"]:
        handle_ex1_item2_graph(json_data)


if __name__ == "__main__":
    main()