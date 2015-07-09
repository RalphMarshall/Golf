#!/usr/bin/perl

use strict;
use warnings;
use feature qw(say);

my ($look, @input);

sub getChar() {
    $look = shift @input;
}

sub error($) {
    my ($msg) = @_;
    say STDERR "Error: $msg.";
}

sub abort($) {
    my ($msg) = @_;
    error($msg);
    die "Halting\n";
}

sub expected($) {
    my ($msg) = @_;
    abort("$msg Expected");
}

sub match($) {
    my ($char) = @_;
    if ($look eq $char) {
        getChar();
    } else {
        expected("''$char''");
    }
}

sub isAlpha($) {
    my ($char) = @_;
    return $char =~ /[A-Z]/i;
}

sub isDigit($) {
    my ($char) = @_;
    return $char =~ /[0-9]/;
}

sub getName() {
    expected('Name') unless isAlpha($look);
    my $retval = uc $look;
    getChar();

    return $retval;
}

sub getNum() {
    expected('Integer') unless isDigit($look);
    my $retval = $look;
    getChar();

    return $retval;
}

sub emit($) {
    my ($string) = @_;
    print "\t$string";
}

sub emitLn($) {
    my ($string) = @_;
    say "\t$string";
}

sub init(@) {
    my (@args) = @_;
    open (INFILE, "<$args[0]") || die("Unable to open $args[0] as input file");
    @input = <INFILE>;
    getChar();
}

 MAIN: {
     init(@ARGV);
}
