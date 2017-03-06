#!/usr/bin/perl

use strict;

my ($timeStr, $miles) = @ARGV;

# print "Time is $timeStr, distance is $miles miles\n";

$timeStr =~ /((\d{1,2}):)?(\d{2}):(\d{2})/;
my ($hours, $mins, $secs) = ($2, $3, $4);

my $decHours = $hours + $mins/60 + $secs/3600;
my $decMins = $decHours * 60;
my $mph = sprintf("%.1f", $miles/$decHours);
my $decPace = $decMins/$miles;
my $paceMins = int($decPace);
my $paceSecs = int(($decPace-$paceMins)*60);

# print "Hours: $hours, minutes: $mins, seconds: $secs, decimal hours: $decHours, decimal minutes: $decMins\n";
print "Speed = $mph MPH, pace = $paceMins:$paceSecs/mile\n";


      
