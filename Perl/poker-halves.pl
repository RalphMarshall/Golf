#!/usr/bin/perl

sub doTape(@) {

    @r=();foreach ((<>)[2,2,3,3,4]){push@r,$1 while(/([CDSH])/g)};print $r[0].@r
};

my @curr;
while (<DATA>) {

    push @curr, $_;
    unless ($. % 5) {
        print doTape(@curr), "\n";
        @curr=();
    }
}

__END__
 _________
|         |
|         |
|         |
-----H-----
 _________
|         |
|    H    |
|         |
-----H-----
 _________
|         |
|  H   H  |
|         |
-----------
 _________
|         |
|  H   H  |
|         |
-----H-----
 _________
|         |
|  H   H  |
|         |
---H---H---
 _________
|         |
|  H   H  |
|         |
---H-H-H---
 _________
|         |
|  H   H  |
|  H   H  |
-----------
 _________
|         |
|  H   H  |
|  H   H  |
-----H-----
 _________
|         |
|  H   H  |
|  H   H  |
---H---H---
 _________
|         |
|  H   H  |
|  H   H  |
---H-H-H---
 _________
|         |
|  H   H  |
|  H H H  |
---H---H---
 _________
|         |
|  H H H  |
|  H   H  |
---H-H-H---