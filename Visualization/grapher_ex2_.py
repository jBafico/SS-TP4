from load_most_recent_json import load_most_recent_simulation_json_ex2
import json

def main():
    with open("item2config.json","r") as f:
        config = json.load(f)
    json_data = load_most_recent_simulation_json_ex2("../outputs")


    

        






if __name__ == "__main__":
    main()