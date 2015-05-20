#!/usr/bin/perl

use strict;
use warnings;

# First round
if (!@ARGV) {
    print "ok\n";
    exit;
}

my ($self, @rest) = @ARGV;

my ($myAction, $myPower, $myLife,
    $myMaxAttacker, $myMaxAttackerId, 
    $maxLife, $maxLifeId, 
    $maxPower, $maxPowerId,
    $totLife, $totPower, $living, $amWeak, $numAttackers) = ('D');

# First, get a situation report
for (@rest) {
    my ($id, $life, $power, $action) = split(',');

    # Let the dead rest in peace
    next unless $life>0;

    if ($id == $self) {
        # Keep track of my own power and life for later comparison
        $myPower = $power;
        $myLife = $life;
        next;
    }

    $living++;
    $numAttackers++ if ($action ne 'D');
    $totPower += $power;
    $totLife += $life;

    if ($action == $self) {
        # Bastard hit me!
        if ($power > $myMaxAttacker) {
            $myMaxAttacker = $power;
            $myMaxAttackerId = $id;
        }
    }

    # If you're going to pick a fight, go for the biggest
    # guy in the room.
    if ($life > $maxLife) {
        $maxLife = $life;
        $maxLifeId = $id;
    }

    # Or, go for the guy with the biggest gun
    if ($power > $maxPower) {
        $maxPower = $power;
        $maxPowerId = $id;
    }
}

# If I'm being hit any attacks are back at the strongest attacker,
# otherwise simply the healthiest opponent overall
my $preferredTarget = $myMaxAttackerId;
$preferredTarget = $maxLifeId unless defined $preferredTarget;

# Check to see if I have below-average life, in which case it's time to get moving
$amWeak = $myLife < $totLife/$living;

# Now figure out what to do
if (!$numAttackers) {
    # Everybody is standing around, so let's mix it up
    $myAction = $preferredTarget;
} elsif (defined $myMaxAttackerId) {
    # My momma told me never to stand there and be hit
    $myAction = $myMaxAttackerId;
} elsif ($amWeak || $living == 1) {
    # Either we're down to two bots, or I'm fairly weak. Atack!!!
    $myAction = $preferredTarget;
} elsif ($myPower < $totPower/$living) {
    # Just lash out at random so we do not lose all of
    # our power through permanent defense
    $myAction = $preferredTarget;
} else { 
    # Work up some courage/power by drinking beer
    # in the corner. Use the default defensive action in this case.
    # Else clause exists just for debugging.
}

print "$myAction\n";
