#! bin/sh

echo "Server Go....!"

echo -n "How many server? : "

default=8001

read num



for i in `seq $num`
do
    echo " `expr $default + $i` "
done


for i in `seq $num`
do
    fuser -k "`expr $default + $i`/tcp"
    java Server "`expr $default + $i`" &
done
