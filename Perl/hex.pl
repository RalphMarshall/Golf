#!/usr/bin/perl

use strict;
use warnings;
use POSIX;

$/=undef;
my $hexen = <DATA>;
$hexen =~ tr/\n/:/;
my %substrs;

my $inputLen = length $hexen;

for my $len (3..3) {

    %substrs=();

    while ($hexen =~ /(.{$len,$len})/g) {
#        print "#$`#$&#$'#\n";
        $substrs{$1} = $substrs{$1} ? $substrs{$1}+1 : 1;
    }

    my $numKeys = keys %substrs;

    for my $keys (sort keys %substrs) {
        print "$keys: $substrs{$keys}\n";
    }

    my $pattSpace = ceil($inputLen/$len);
    my $substringSpace = $numKeys * $len;
    my $totalSpace = $pattSpace + $substringSpace;

    print "Length $len: Space needed is $totalSpace ($pattSpace for pattern and $substringSpace for $numKeys substrings)\n";
}

my (%encoding,@reverse);

my $r = 1;
for (keys %substrs) {
    print "Making encoding of key=$_, value = $r\n";
    $encoding{$_} = $r;
    $reverse[$r]=$_;
        $r++;
}

for (keys %encoding) {
    print "$_ => $encoding{$_}\n";
}

print "Initial state is $hexen\n";
my $didRepl = 1;
while ($didRepl) {

    $didRepl=0;
    for (keys %encoding) {
        if ($hexen =~ s[^(\d*)$_][$1$encoding{$_}]) {
            $didRepl = 1;
            print "Match on $_: $hexen\n";
        }
    }
}

print "Converted: $hexen\n";
my $restored = $hexen;
for (0..$#reverse) {

    print "Looking to replace $_\n";
    next unless defined $reverse[$_];
    
    print "Attempting to substitute $_ with $reverse[$_]\n";
    # $restored =~ s/$_/"$reverse[$_]"/eg;
}

$restored =~ tr[fb:][/\\\n];
print "Restored: $restored\n";


__END__
       __
    __f  b__
 __f  b__f  b__
f  b__f  b__f  b
b__f  b__f  b__f
f  b__f  b__f  b
b__f  b__f  b__f
f  b__f  b__f  b
b__f  b__f  b__f
   b__f  b__f
      b__f
