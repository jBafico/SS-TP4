import math

from matplotlib.ticker import ScalarFormatter
import numpy as np
from load_most_recent_json import load_most_recent_simulation_json_ex2
import json
import os
import matplotlib.pyplot as plt
import imageio
import io

output_directory = "EJ2"
animation_directory = "Animations"

TRIES = 1000


def SuperScriptinate(number):
    return number.replace('0', '⁰').replace('1', '¹').replace('2', '²').replace('3', '³').replace('4', '⁴').replace('5', '⁵').replace('6', '⁶').replace('7',
                                                                                                                                                        '⁷').replace(
        '8', '⁸').replace('9', '⁹').replace('-', '⁻')


def sci_notation(number, sig_fig=2):
    ret_string = "{0:.{1:d}e}".format(number, sig_fig)
    a, b = ret_string.split("e")
    b = int(b)  # removed leading "+" and strips leading zeros too.
    return a + "x10" + SuperScriptinate(str(b))


def reduce_to_slope(xs, ys):
    Y_INTERCEPT = 0
    LINEAR_FUNCTION = lambda x, m: m * math.sqrt(x) + Y_INTERCEPT

    candidate_slope = 0
    min_error = np.inf
    best_d = None

    max_slope = max(ys) / max(xs)
    slopes = np.arange(0, max_slope * 2, max_slope / TRIES)

    xs_out = []
    ys_out = []
    for slope in slopes:
        d = slope / 2
        error = 0
        for x, y in zip(xs, ys):
            error += (y - LINEAR_FUNCTION(x, slope)) ** 2
        if error < min_error:
            min_error = error
            candidate_slope = slope
            best_d = d
        xs_out.append(d)
        ys_out.append(error)

    return candidate_slope, min_error, xs_out, ys_out, best_d


def obtain_error_adjustment_graph(x_values, y_values):
    _, min_error, xs_out, ys_out, best_d = reduce_to_slope(x_values, y_values)

    ax = plt.gca()
    ax.xaxis.set_major_formatter(ScalarFormatter(useMathText=True))
    ax.ticklabel_format(style='sci', axis='x', scilimits=(0, 0))

    # Y-axis formatting
    ax.yaxis.set_major_formatter(ScalarFormatter(useMathText=True))
    ax.ticklabel_format(style='sci', axis='y', scilimits=(0, 0))

    plt.scatter(x=xs_out, y=ys_out)
    plt.xlabel("D (m\u00b2/s)")
    plt.ylabel("Error (m\u00b2)")

    plt.axvline(x=best_d, color='r', linestyle='--', label=f"Error={sci_notation(min_error)} Coeficiente Defusion = {sci_notation(best_d)}")
    plt.legend()

    output_name = "cuadratic_error_graph.png"
    output_file = os.path.join(output_directory, output_name)
    os.makedirs(output_directory, exist_ok=True)
    plt.savefig(output_file)
    plt.close()


def main():
    with open("item2config.json", "r") as f:
        config = json.load(f)

    json_data = load_most_recent_simulation_json_ex2("../outputs")

    if config["animations"]:
        results_by_k_dict: dict[str, dict[str, list[list[dict[str, float]]]]] = json_data['resultsByKAndW']
        generate_animations(results_by_k_dict)
    
    amplitude_vs_time_graph(json_data)
    max_oscilation_amplitudes_by_k_and_w: dict[str, dict[str, list[float]]] = calc_max_oscilation_amplitudes_by_w(json_data)
    if config["item1and2graphs"]:
        amplitude_vs_omega_graphs_for_different_k(max_oscilation_amplitudes_by_k_and_w)
    if config["item3graph"]:
        aproximation_w_sqrt_k_graph(max_oscilation_amplitudes_by_k_and_w)


def amplitude_vs_time_graph(data:dict[str, dict[str, list[float]]]):
    tf= data["params"]["tf"]
    results_by_k_dict: dict[str, dict[str, list[list[dict[str, float]]]]] = data['resultsByKAndW']
    ensure_output_directory_creation(output_directory)
    for k, w_to_particle_dict in results_by_k_dict.items():
        for current_w, particle_list_per_timestep in w_to_particle_dict.items():
            times= np.arange(0, tf, 1/(100*float(current_w)))
            for i, timestep in enumerate(times):
                # Gather all xPositions for the current timestep
                x_positions = [
                    particle["position"]
                    for particle in particle_list_per_timestep[i]
                    if "position" in particle
                ]
                
                # Plot all particle positions at this timestep
                plt.scatter([timestep] * len(x_positions), x_positions, color="blue")

            plt.xlabel("Tiempo (s)")
            plt.ylabel('Amplitud (m)')
            plt.grid(True)

            # Define the output file path with dt in the filename
            file_path = os.path.join(output_directory, f"amplitude_vs_time_for_k_{k}_w_{current_w}.png")

            # Save the plot to the file
            plt.savefig(file_path)

            # Optionally, you can clear the current figure to prevent overlay issues in future plots
            plt.clf()

            print(f"Saved plot to '{file_path}'")


def get_k_to_biggest_w(max_oscilation_amplitudes_by_k_and_w: dict[str, dict[str, list[float]]]):
    k_to_w0: dict[str, float] = {}
    for k, w_to_amplitude_dict in max_oscilation_amplitudes_by_k_and_w.items():
        max_w = None
        max_amplitude = float("-inf")
        for current_w, amplitude_list in w_to_amplitude_dict.items():
            current_max_amplitude = max(amplitude_list)
            if current_max_amplitude > max_amplitude:
                max_amplitude = current_max_amplitude
                max_w = float(current_w)
        k_to_w0[k] = max_w
    return k_to_w0


def aproximation_w_sqrt_k_graph(max_oscilation_amplitudes_by_k_and_w: dict[str, dict[str, list[float]]]):
    k_to_w0 = get_k_to_biggest_w(max_oscilation_amplitudes_by_k_and_w)

    x_values = [float(k) for k in k_to_w0.keys()]
    y_values = [w0 for w0 in k_to_w0.values()]

    ensure_output_directory_creation(output_directory)
    plt.scatter(x_values, y_values)
    plt.plot(x_values, y_values)
    plt.xlabel("k (kg/s²)")
    plt.ylabel('ω (rad/s)')
    plt.grid(True)

    # Define the output file path with dt in the filename
    file_path = os.path.join(output_directory, f"w0_given_k.png")

    # Save the plot to the file
    plt.savefig(file_path)

    # Optionally, you can clear the current figure to prevent overlay issues in future plots
    plt.clf()

    print(f"Saved plot to '{file_path}'")

    obtain_error_adjustment_graph(x_values, y_values)


def calc_max_oscilation_amplitudes_by_w(json_data: dict[str, dict]) -> dict[str, dict[str, list[float]]]:
    # This graph analyses results with different Ws and Ks
    results_by_k_dict: dict[str, dict[str, list[list[dict[str, float]]]]] = json_data['resultsByKAndW']

    # Key: K, Value: Dict with W as key and List of amplitudes for each dt as value
    amplitudes_by_k_and_w_dict: dict[str, dict[str, list[float]]] = {}
    for k, results_for_k in results_by_k_dict.items():
        # Key: W, Value: List of amplitudes for each dt
        amplitudes_by_w_dict: dict[str, list[float]] = {}
        for w, results_for_w in results_for_k.items():
            print(f"Calculating max oscilation amplitude for k={k} and w={w}")
            if (k == '100.00' and w == '15.00') or (k == '500.00'):
                pass
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


def amplitude_vs_omega_graph(max_oscilation_amplitudes_by_w: dict[str, list[float]], current_k: str):
    print('Plotting amplitude vs omega for k =', current_k)
    omega_vals = [float(w) for w in max_oscilation_amplitudes_by_w.keys()]
    amplitude_vals = [max(amplitudes) for amplitudes in max_oscilation_amplitudes_by_w.values()]

    ensure_output_directory_creation(output_directory)
    plt.scatter(omega_vals, amplitude_vals)
    plt.plot(omega_vals, amplitude_vals)
    plt.xlabel('ω (rad/s)')
    plt.ylabel('Amplitud (m)')
    plt.grid(True)

    # Define the output file path with dt in the filename
    file_path = os.path.join(output_directory, f"omega_vs_amplitude_for_K_{current_k}.png")

    # Save the plot to the file
    plt.savefig(file_path)

    # Optionally, you can clear the current figure to prevent overlay issues in future plots
    plt.clf()

    print(f"Saved plot to '{file_path}'")


def amplitude_vs_omega_graphs_for_different_k(max_oscilation_amplitudes_by_k_then_w: dict[str, dict[str, list[float]]]):
    print('Starting to plot')

    for k, wsdict in max_oscilation_amplitudes_by_k_then_w.items():
        amplitude_vs_omega_graph(wsdict, k)


# def amplitude_vs_omega_graph_with_k(amplitude_vals, omega_vals, k_vals):
#     directory = 'Ej2_b'

#     ensure_output_directory_creation(directory)

#     for k in k_vals:
#         # Graficar ω vs amplitud para cada valor de k
#         plt.plot(omega_vals, amplitude_vals, label=f'k = {k:.0f} N/m')

#     plt.xlabel('ω (rad/s)')
#     plt.ylabel('Amplitud (m)')
#     plt.grid(True)

#     # Define the output file path with dt in the filename
#     file_path = os.path.join(directory, f"omega_vs_amplitude_per_k.png")

#     # Save the plot to the file
#     plt.savefig(file_path)

#     # Optionally, you can clear the current figure to prevent overlay issues in future plots
#     plt.clf()

#     print(f"Graphic saved to '{file_path}'")

def generate_animations(data):
    ensure_output_directory_creation(animation_directory)

    for k, results_for_k in data.items():

        for w, results_for_w in results_for_k.items():
            # List to store in-memory images
            frames = []
            print(f"Animation for k={k} and w={w}")
            results_for_w: list[list[dict[str, float]]]
            time=0
            for particles_in_dt in results_for_w:
                amplitudes_by_particle_in_dt = {}
                i = 0
                for particle in particles_in_dt:
                    amplitudes_by_particle_in_dt.setdefault(i, [])
                    amplitudes_by_particle_in_dt[i].append(particle['position'])
                    i += 1
                if time%50==0:
                    frame_buf = plot_simulation_frame_in_memory(amplitudes_by_particle_in_dt.values())
                    frames.append(imageio.imread(frame_buf))
                time+=1

            # Create a GIF directly from in-memory images
            gif_buf = io.BytesIO()
            with imageio.get_writer(gif_buf, format='GIF', mode='I', duration=3) as writer:
                for frame in frames:
                    writer.append_data(frame)

            gif_buf.seek(0)  # Rewind the buffer to read the GIF

            # Save or use the GIF as needed, for example, to save to a file:
            with open(f"./Animations/simulation_k_{k}_w_{w}.gif", "wb") as f:
                print(f"outputing gif _k_{k}_w_{w}")
                f.write(gif_buf.getvalue())


def ensure_output_directory_creation(directory):
    # Check if the directory exists, if not, create it
    if not os.path.exists(directory):
        os.makedirs(directory)
        print(f"Directory '{directory}' created.")
    else:
        print(f"Directory '{directory}' already exists.")


# Function to plot a single simulation frame and return as an in-memory image
def plot_simulation_frame_in_memory(particles):
    fig, ax = plt.subplots()

    plt.scatter(np.arange(len(particles)), particles, linewidth=2)

    # Set limits and aspect ratio
    ax.set_ylim(-1, 1)

    # Add labels and title
    ax.set_xlabel('Particula')
    ax.set_ylabel('Posición (m)')

    # Save the frame as an in-memory image (BytesIO)
    buf = io.BytesIO()
    plt.savefig(buf, format='png')
    plt.close(fig)
    buf.seek(0)  # Rewind the buffer
    print("I did an image")

    return buf


if __name__ == "__main__":
    main()
