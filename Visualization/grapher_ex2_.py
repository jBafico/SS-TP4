from load_most_recent_json import load_most_recent_simulation_json_ex2
import json
import numpy as np
import matplotlib.pyplot as plt

def main():
    with open("item2config.json","r") as f:
        config = json.load(f)
    json_data = load_most_recent_simulation_json_ex2("../outputs")



def amplitude_vs_omega_graph(amplitude_vals, omega_vals):

    directory= 'Ej2_a'

    ensure_output_directory_creation(directory)
    plt.plot(omega_vals, amplitude_vals)
    plt.xlabel('ω (rad/s)')
    plt.ylabel('Amplitud (m)')
    plt.grid(True)
    
    # Define the output file path with dt in the filename
    file_path = os.path.join(directory, f"omega_vs_amplitude.png")

    # Save the plot to the file
    plt.savefig(file_path)

    # Optionally, you can clear the current figure to prevent overlay issues in future plots
    plt.clf()

    print(f"Graphic saved to '{file_path}'")


def amplitude_vs_omega_graph_with_k(amplitude_vals, omega_vals, k_vals):

    directory= 'Ej2_b'

    ensure_output_directory_creation(directory)
    
    for k in k_vals:
        # Graficar ω vs amplitud para cada valor de k
        plt.plot(omega_vals, amplitude_vals, label=f'k = {k:.0f} N/m')

    plt.xlabel('ω (rad/s)')
    plt.ylabel('Amplitud (m)')
    plt.grid(True)
    
    # Define the output file path with dt in the filename
    file_path = os.path.join(directory, f"omega_vs_amplitude_per_k.png")

    # Save the plot to the file
    plt.savefig(file_path)

    # Optionally, you can clear the current figure to prevent overlay issues in future plots
    plt.clf()

    print(f"Graphic saved to '{file_path}'")

def ensure_output_directory_creation(directory):
    # Check if the directory exists, if not, create it
    if not os.path.exists(directory):
        os.makedirs(directory)
        print(f"Directory '{directory}' created.")
    else:
        print(f"Directory '{directory}' already exists.")

if __name__ == "__main__":
    main()