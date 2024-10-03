import os
from matplotlib import pyplot as plt

directory = "ex_1_2_output"

def ensure_output_directory_creation():
    # Check if the directory exists, if not, create it
    if not os.path.exists(directory):
        os.makedirs(directory)
        print(f"Directory '{directory}' created.")
    else:
        print(f"Directory '{directory}' already exists.")

def create_first_graphic(dt, analytical, beeman, gear5, verlet):
    # Ensure the output directory exists
    ensure_output_directory_creation()

    x_values = []
    y_values_func1 = []
    y_values_func2 = []
    y_values_func3 = []
    y_values_func4 = []

    for i in range(len(analytical)):
        y_values_func1.append(analytical[i]["r0"])
        y_values_func2.append(beeman[i]["r0"])
        y_values_func3.append(gear5[i]["r0"])
        y_values_func4.append(verlet[i]["r0"])
        x_values.append(analytical[i]["time"])

    # Plot each function
    plt.plot(x_values, y_values_func1, label="analítica")
    plt.plot(x_values, y_values_func2, label="beeman")
    plt.plot(x_values, y_values_func3, label="gear5")
    plt.plot(x_values, y_values_func4, label="verlet")

    # Add title and labels
    plt.title(f"Comparación de métodos con dt = {dt}")  # Include dt in the title
    plt.xlabel("tiempo (s)")
    plt.ylabel("posición (m)")

    # Add a legend
    plt.legend()

    # Define the output file path with dt in the filename
    file_path = os.path.join(directory, f"comparison_graph_dt_{dt}.png")

    # Save the plot to the file
    plt.savefig(file_path)

    # Optionally, you can clear the current figure to prevent overlay issues in future plots
    plt.clf()

    print(f"Graphic saved to '{file_path}'")

# Example usage:
# create_first_graphic(0.01, analytical_data, beeman_data, gear5_data, verlet_data)



def create_ECM_graph(repetitions_data: dict):

    acummulation_dic : dict[float,list] = {}


    # populate dict
    result_section = repetitions_data["0"]
    for current_dt_list in result_section:
        acummulation_dic[current_dt_list["dt"]] = []

    


    print("")

    for current_repetition_values in repetitions_data.values():
        current_dt = current_repetition_values["dt"]

    
        






def handle_ex1_item2_graph(json_data):
    ensure_output_directory_creation()

    result_section = json_data["resultsByRepetitionNo"]["0"]
    for current_dt_list in result_section:
        create_first_graphic(current_dt_list["dt"], current_dt_list["analytical"],current_dt_list["beeman"],current_dt_list["gear5"],current_dt_list["verlet"])


    create_ECM_graph(json_data["resultsByRepetitionNo"])

