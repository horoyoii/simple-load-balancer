#! /bin/sh


echo "---------------------"
echo "| how many client ? |"
echo "---------------------"
echo -n "port num : "
read port

echo -n "number : "

read num

for i in `seq $num`
do
    java Client $port $i &
done
