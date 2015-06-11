#!/usr/bin/perl

use strict;
use warnings;

$/ = undef;
my $input = <DATA>;

$input =~ tr/\n\r/ /;

if ($input =~ m@/\*.*?\*/@s) {
    print "Matched on $&\n";
} else {
    print "No match for $input\n";
}

__END__
/*!
 * jQuery UI CSS Framework 1.8.21
 *
 * Copyright 2012, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Theming/API
 */
