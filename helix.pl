#!/usr/bin/perl

my ($left, $mid, $right) = ('..f:.0.:..b', 'b.f/:.f.:f.b', 'b..:.0.:f..');

my $n = 1;
my $crosses = $n-1;

sub doPrint($$) {

    my ($pat, $rowNum) = @_;
    
    my ($t, $m, $b) = split(':', $pat =~ tr[bf.][\/ ]r);
    my (@t) = split '', $t;
    my (@m) = split '', $m;
    my (@b) = split '', $b;

    print $t if $rowNum==0;
    print $m if $rowNum==1;
    print $b if $rowNum==2;
    print "\n";
}

for my $row (0..2) {
    doPrint($left, $row);
    for (1..$crosses) {
        doPrint($mid, $row);
    }
    doPrint($right, $row);
}
