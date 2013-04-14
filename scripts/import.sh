#!/bin/sh

echo "Generating import script"
out='/tmp/import.sql'
rm $out
cat scripts/import.sql > $out
python scripts/current.py >> $out
cat scripts/finish.sql >> $out

echo "Please run something like mysql < $out"

