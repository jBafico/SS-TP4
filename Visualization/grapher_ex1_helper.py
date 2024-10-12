import os
from matplotlib import pyplot as plt
import numpy as np

directory = "ex_1_output"

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
        y_values_func1.append(analytical[i]["position"])
        y_values_func2.append(beeman[i]["position"])
        y_values_func3.append(gear5[i]["position"])
        y_values_func4.append(verlet[i]["position"])
        x_values.append(analytical[i]["time"])

    # Plot each function
    # Plot scatter points and lines for each function
    plt.scatter(x_values, y_values_func1, label="analítica")  
    plt.plot(x_values, y_values_func1, linestyle='--')  

    plt.scatter(x_values, y_values_func2, label="beeman")
    plt.plot(x_values, y_values_func2, linestyle='--')

    plt.scatter(x_values, y_values_func3, label="gear5") 
    plt.plot(x_values, y_values_func3, linestyle='--')

    plt.scatter(x_values, y_values_func4, label="verlet") 
    plt.plot(x_values, y_values_func4, linestyle='--')


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




    
        






def handle_ex1_item2_graph(json_data):
    ensure_output_directory_creation()

    result_section = json_data["resultsByRepetitionNo"]["0"]
    for current_dt_list in result_section:
        create_first_graphic(current_dt_list["dt"], current_dt_list["analytical"],current_dt_list["beeman"],current_dt_list["gear5"],current_dt_list["verlet"])


def calculate_ecm(analytical_solution , integration_solution):
    error_array = []
    for analytical_value, integration_value in zip(analytical_solution,integration_solution):
        error_array.append( (analytical_value["position"] - integration_value["position"]) ** 2 )
    return np.mean(error_array)


def graphic_item3(error_dictionary):
    # Create a new figure
    plt.figure()

    # Iterate over the error dictionary
    for title, values in error_dictionary.items():
        # Extract x and y values from the dictionary
        x_values = list(values.keys())
        y_values = list(values.values())
        
        # Plot the scattered points
        plt.scatter(x_values, y_values, label=f'{title} (scatter)')
        
        # Plot the line connecting the points
        plt.plot(x_values, y_values)

    # Set both x and y axes to logarithmic scale

    # Add labels for the axes
    plt.xlabel("tiempo (s)")
    plt.ylabel("posición (m)")
    plt.legend()

    plt.xscale('log')
    plt.yscale('log')
    # Define the output file path
    file_path = os.path.join(directory, "ECM_GRAPHIC_LOG.png")

    # Save the plot to the file
    plt.savefig(file_path)

    # Optionally, you can clear the current figure to prevent overlay issues in future plots
    plt.clf()

    print(f"Graphic ECM with logarithmic scale saved to '{file_path}'")

def handle_ex1_item3_graph(json_data):
    ensure_output_directory_creation()


    error_dictionary : dict[float,dict[str,float]] = {}
    error_dictionary["beeman"] = {}
    error_dictionary["verlet"] = {}
    error_dictionary["gear5"] = {}

    result_section = json_data["resultsByRepetitionNo"]["0"]
    for current_dt_list in result_section:
        current_dt = current_dt_list["dt"]
        error_dictionary["beeman"][current_dt] = calculate_ecm(current_dt_list["analytical"],current_dt_list["beeman"])
        error_dictionary["gear5"][current_dt] = calculate_ecm(current_dt_list["analytical"],current_dt_list["gear5"])
        error_dictionary["verlet"][current_dt] = calculate_ecm(current_dt_list["analytical"],current_dt_list["verlet"])
    
    graphic_item3(error_dictionary)

    

        





