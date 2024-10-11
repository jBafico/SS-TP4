from load_most_recent_json import load_most_recent_simulation_json_ex2
import json
import os
import matplotlib.pyplot as plt


def main():
    # with open("item2config.json", "r") as f:
    #     config = json.load(f)
    json_data = load_most_recent_simulation_json_ex2("../outputs")
    max_oscilation_amplitudes_by_k_and_w: dict[str, dict[str, list[float]]] = calc_max_oscilation_amplitudes_by_w(json_data)
    max_oscilation_amplitudes_for_k_100: dict[str, float] = {w: max(amplitudes) for w, amplitudes in max_oscilation_amplitudes_by_k_and_w['100.0'].items()}
    amplitude_vs_omega_graph(max_oscilation_amplitudes_by_k_and_w['100.0'])
    pass


def calc_max_oscilation_amplitudes_by_w(json_data: dict[str, dict]) -> dict[str, dict[str, list[float]]]:
    # This graph analyses results with different Ws and K = 100
    results_by_k_dict: dict[str, dict[str, list[list[dict[str, float]]]]] = json_data['resultsByKAndW']

    # Key: K, Value: Dict with W as key and List of amplitudes for each dt as value
    amplitudes_by_k_and_w_dict: dict[str, dict[str, list[float]]] = {}
    for k, results_for_k in results_by_k_dict.items():
        # Key: W, Value: List of amplitudes for each dt
        amplitudes_by_w_dict: dict[str, list[float]] = {}
        for w, results_for_w in results_for_k.items():
            print(f"Calculating max oscilation amplitude for k={k} and w={w}")
            results_for_w: list[list[dict[str, float]]]
            for particles_in_dt in results_for_w:
                max_oscilation_amplitude = calc_max_oscilation_amplitude_in_dt(particles_in_dt)
                amplitudes_by_w_dict.setdefault(w, [])
                amplitudes_by_w_dict[w].append(max_oscilation_amplitude)
        amplitudes_by_k_and_w_dict[k] = amplitudes_by_w_dict
    return amplitudes_by_k_and_w_dict


def calc_max_oscilation_amplitude_in_dt(particles_in_dt: list[dict[str, float]]) -> float:
    max_oscilation_amplitude = 0
    for particle in particles_in_dt:
        if particle['position'] > max_oscilation_amplitude:
            max_oscilation_amplitude = particle['position']
    return max_oscilation_amplitude


def amplitude_vs_omega_graph(max_oscilation_amplitudes_by_w):
    directory = 'Ej2_a'
    print('Starting to plot')
    omega_vals = [float(w) for w in max_oscilation_amplitudes_by_w.keys()]
    amplitude_vals = [max(amplitudes) for amplitudes in max_oscilation_amplitudes_by_w.values()]

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

    print(f"Saved plot to '{file_path}'")


def amplitude_vs_omega_graph_with_k(amplitude_vals, omega_vals, k_vals):
    directory = 'Ej2_b'

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
