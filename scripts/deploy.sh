#!/bin/bash

echo "$(pwd)"
chmod 600 ./travis/id_rsa
mv ./travis/id_rsa ~/.ssh/id_rsa
echo "Host *" >> ~/.ssh/config
echo "  BatchMode yes" >> ~/.ssh/config
ssh totalizator@encoders.kiev.ua /home/totalizator/update.sh

