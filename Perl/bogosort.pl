#!/usr/bin/perl -w
my @m=(3, 1, 4, 9, 5, 2, 7, 8, 6);

sub notSorted{for(0..7){return 1 if$m[$_]>$m[$_+1]}return 0}
while(notSorted){$l=rand(8);splice @m,$l,2,$m[$l+1],$m[$l]if $m[$l]>$m[$l+1]}

print join(", ", @m), "\n";

@m=(3, 1, 4, 9, 5, 2, 7, 8, 6);

    {for(0..7){if($m[$_]>$m[$_+1]){splice@m,$_,2,$m[$_+1],$m[$_];redo}}}

# Ungolfed code
# 
#      o: {                                             # Label for redo; runs once unless redo is called
#          for (0..7) {                                 # Loop index is in $_
#              if ($m[$_] > $m[$_+1]) {                 # Check if swap needed
#                  splice @m, $_, 2, $m[$_+1], $m[$_];  # Replace a two element slice of
#                                                       # the array with those two elements reversed
#                  redo o                               # Start the labeled block over
#              }
#          }
#     }

print join(", ", @m), "\n";
