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

sub emit(@) {
    print "\t", @_;
}

sub emitLn(@) {
    say "\t", @_;
}

sub init(@) {
    @input = (split '', <>);
    getChar();
}

################################################################
# Lesson Two
################################################################

sub term() {
    emitLn('$d0 = ', getNum(), ";");
}

sub expression() {
    term();

    while ($look =~ /[+-]/) {
        emitLn('$d1 = $d0;');

        if ($look eq '+') {
            add();
        } elsif ($look eq '-') {
            subtract();
        } else {
            expected("Add operator");
        }
    }
}

sub add() {
    match("+");
    term();
    emitLn('$d0 += $d1;');
}

sub subtract() {
    match("-");
    term();
    emitLn('$d0 -= $d1;');
    emitLn('$d0 *= -1;');
}

 MAIN: {
     init();
     expression();
}
