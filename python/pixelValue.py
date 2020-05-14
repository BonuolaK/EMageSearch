import cv2
from collections import Counter
import argparse
import webcolors
import numpy as np
import matplotlib.pyplot as plt
from sklearn.cluster import KMeans


ap = argparse.ArgumentParser()
ap.add_argument("-i", "--image", required=True,
	help="path to input image")
args = vars(ap.parse_args())


def closest_colour(requested_colour):
    min_colours = {}
    for key, name in webcolors.css3_hex_to_names.items():
        r_c, g_c, b_c = webcolors.hex_to_rgb(key)
        rd = (r_c - requested_colour[0]) ** 2
        gd = (g_c - requested_colour[1]) ** 2
        bd = (b_c - requested_colour[2]) ** 2
        min_colours[(rd + gd + bd)] = name
    return min_colours[min(min_colours.keys())]

def get_colour_name(requested_colour):
    try:
        closest_name = actual_name = webcolors.rgb_to_name(requested_colour)
    except ValueError:
        closest_name = closest_colour(requested_colour)
        actual_name = None
    return actual_name, closest_name


img = cv2.imread(args["image"])
img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)

img = img.reshape((img.shape[0] * img.shape[1],3)) #represent as row*column,channel number
clf = KMeans(n_clusters=3) #cluster number
labels = clf.fit_predict(img)

counts = Counter(labels)

center_colors = clf.cluster_centers_
# We get ordered colors by iterating through the keys
ordered_colors = [center_colors[i] for i in counts.keys()]
rgb_colors = [ordered_colors[i] for i in counts.keys()]
color = rgb_colors[0]

red_color = int(color[0])
green_color = int(color[1])
blue_color = int(color[2])

print("{},{},{}".format(red_color, green_color,blue_color))


# actual_name, closest_name = get_colour_name((int(color[0]), int(color[1]), int(color[2])))

# print(closest_name)

