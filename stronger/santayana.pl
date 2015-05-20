#!/usr/bin/perl

use strict;
use warnings;

# First round
if (!@ARGV) {
    print "ok\n";
    exit;
}

# Read in our multi-round/multi-game state information
my $state;
if (open STATE, "state/santayana.out") {
    $state = <STATE>;
    close STATE;
}

# Stuff the historical data into a hash keyed by opponent ID
my %state;
my @state = $state ? split(' ', $state) : ();
for (@state) {
    my ($id, $life, $power) = split ',';
    $state{$id} = [$life, $power];
}

my ($self, @rest) = @ARGV;

my ($maxLife, $maxLifeId, $living, $amWeak) = (0, undef, 0, 0);

# First, get a situation report
for (@rest) {
    my ($id, $life, $power, $action) = split(',');

    # Update the historical hash with latest information
    my $aref = $state{$id};
    if ($aref) {
    $$aref[0] += $life;
    $$aref[1] += $power;
    } else {
    $state{$id} = [$life, $power];
    }

    # Let the dead rest in peace
    next unless $life;

    if ($id == $self) {
        # See how strong we are and then
        # move on to the next bot
        $amWeak = $life < 200;
        next;
    }

    $living++;

    # Our target is based on the historically 
    # strongest opponent, independent of current state, 
    # unless they are actually dead
    if ($life > 0 && $state{$id}->[0] > $maxLife) {
    $maxLife = $state{$id}->[0];
    $maxLifeId = $id;
    }
}

# Write out the latest state for next time around
if (open STATE, ">state/santayana.out") {
    print STATE join(" ", map { join ",", $_, $state{$_}->[0], $state{$_}->[1] } keys %state);
    close STATE;
}

# Now figure out what to do
if (defined $maxLifeId) {
    # Should always be defined, but who knows
    print "$maxLifeId\n";
} else {
    print "D\n";
}
