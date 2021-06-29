#!/bin/bash

./darknet detector demo yolov3.data yolov3-tiny.cfg yolov3-tiny_7classes_229824.weights -ext_output -c 1
