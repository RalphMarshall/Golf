#!/usr/bin/perl
use strict;
use warnings;
use utf8;
use charnames ':full';

my ($fh, $rfh);
my ($vname, $errorcode, $message, $col, $byte);
open($rfh, '>>:encoding(UTF-8)', 'utf8report.txt') || die 'Error opening file';
for (0x100..0x9fff) { # You may want to change these numbers if the script runs for too long
    open($fh, '>:encoding(UTF-8)', 'utf8test.pl') || die 'Error opening file';
    print $fh "use utf8;\n";
    $vname = pack "U", $_;
    print $fh "my \$$vname = $_;\nprint \$$vname;";
    close $fh;
    #system "perl", "-c", "utf8test.pl";
    $message = `perl -c utf8test.pl 2>&1`;
    ($byte, $col) = $message =~ /character \\x(..).*column (\d)/;
    $errorcode = ($? >> 8) ? "FAIL at byte ".($col-4)."($byte)" : "PASS";
    print $rfh "$_\t$errorcode, character ".(charnames::viacode($_))." \n";
    print $_-$_%100,"\r";
}
print "\n";
close $rfh;

ٰ
    
