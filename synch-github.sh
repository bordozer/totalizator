#!/bin/bash

git checkout master;

git push github master;

git checkout public;

git rebase  master;

git push github public;

git checkout master;