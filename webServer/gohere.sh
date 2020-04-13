#!/bin/bash -l

# Save this file in /usr/local/bin and chmod a+x for easy access

# Execute gohere to switch $GOPATH to current directory
# Type exit to switch back

# Example usage:
# $ echo $GOPATH
#
# $ gohere
# (go@directory) $ echo $GOPATH
# /path/to/directory
# (go@directory) $ exit
# exit
# $ echo $GOPATH
#
# $

# Set $GOPATH to current directory
export GOPATH=${PWD}

# Update command prompt to remind you which workspace you're using.
# This requires that your .bashrc looks for the $PROMPT_PREFIX variable
export PROMPT_PREFIX="(go@`basename ${PWD}`)"

# Start a new bash shell to inherit $GOPATH
zsh
