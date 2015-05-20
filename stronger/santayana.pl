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
    
    my ($maxPower, $maxOpponentId, $living) = (0, undef, 0);
    
    # First, get a situation report
    for (@rest) {
        my ($id, $life, $power, $action) = split(',');
    
        # Let the dead rest in peace
        next unless $life > 0;
    
        $living++;
    
        # Update the historical hash with latest information
        my $aref = $state{$id};
        if ($aref) {
            $$aref[0] += $life * ($action eq 'D' ? 1 : 1.5);
            $$aref[1] += $power;
        } else {
            $state{$id} = [$life, $power];
        }
    
        next if ($id == $self);
    
        # Our target is based on the historically
        # strongest opponent, independent of current state,
        # unless they are actually dead
        if ($life > 0 && $state{$id}->[1] > $maxPower) {
            $maxPower = $state{$id}->[1];
            $maxOpponentId = $id;
        }
    }
    
    # Write out the latest state for next time around
    if (open STATE, ">state/santayana.out") {
        print STATE join(" ", map { join ",", $_, $state{$_}->[0], $state{$_}->[1] } sort { $state{$b}->[0] <=> $state{$a}->[0]} keys %state);
        close STATE;
    }
    
    # Now figure out what to do
    if (defined $maxOpponentId) {
        # Should always be defined, but who knows
        print "$maxOpponentId\n";
    } else {
        print "D\n";
    }
