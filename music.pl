#!/usr/bin/perl -p

# use strict;
# use warnings;
# 
# sub doSearch($) {
#     my ($string) = @_;
# 
#     my $searchLen = length $string;
#     while ($searchLen > 0) {
#         return "$& $searchLen" if $string =~ /[a-gA-G]{$searchLen,$searchLen}/;
#         $searchLen--;
#     }
# 
#     return 0;
# }
# 
# while (<DATA>) {
#     chomp;
#     my $result = doSearch(tr /a-zA-Z//cdr);
#     print "Result for $_ is $result\n";
# }

use feature qw(say);

$\=0;y/a-zA-Z//cd;$i=length;while($i&&!$&){"$& $i"if/[a-g]{$i}/i;$i--}

__DATA__
"FEED ME!" I'm hungry!"
No no no, no musistrin!
"A **bad** !!!fAd82342"
"Good golfing!"
