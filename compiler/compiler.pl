#!/usr/bin/perl

use strict;
use warnings;
use feature qw(say);

my ($look, @input, @program);

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

sub isAddop($) {
    my ($char) = @_;
    return $char =~ /[+-]/;
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
    push @program, "\t" . join('', @_);
    # print "\t", @_;
}

sub emitLn(@) {
    push @program, "\t" . join('', @_);
    # say "\t", @_;
}

sub init(@) {
    @input = (split '', <>);
    @program = ("\t" . 'my ($d0, $d1, @stack);');
    getChar();
}

################################################################
# Lesson Two
################################################################

sub term() {
    factor();
    while ($look =~ m{[*/]}) {
        emitLn('push @stack, $d0;            # LHS of ', $look);
        if ($look eq '*') {
            multiply();
        } elsif ($look eq '/') {
            divide();
        } else {
            expected("Multiply operator");
        }
    }
}

sub expression() {

    if (isAddop($look)) {
        emitLn('$d0 = 0;                     # Unary ', $look);
    } else {
        term();
    }

    while ($look =~ /[+-]/) {
        emitLn('push @stack, $d0;            # LHS of ', $look);

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
    emitLn('$d0 += pop @stack;           # RHS of +');
}

sub subtract() {
    match("-");
    term();
    emitLn('$d0 -= pop @stack;           # RHS of -');
    emitLn('$d0 *= -1;');
}

my $nestingLevel = 0;
sub factor() {
    if ($look eq '(') {
        match('(');
        $nestingLevel++;
        emitLn("                             # Open paren level $nestingLevel");
        expression();

        emitLn("                             # Close paren level $nestingLevel");
        $nestingLevel--;
        match(')');
    } else {
        my $num = getNum();
        emitLn('$d0 = ', $num, ";                     # Literal $num");
    }
}

sub multiply() {
    match("*");
    factor();
    emitLn('$d0 *= pop @stack;           # RHS of Multiplication');
}

sub divide() {
    match("/");
    factor();
    emitLn('$d1 = pop @stack;            # RHS of Division');
    emitLn('$d0 = $d1/$d0;');
}

################################################################
# Lesson Three
################################################################



 MAIN: {
     init();
     expression();

     say join("\n", @program);

     my $totProg = join("\n", @program);
     my $result = eval($totProg) || die "Failed to evaluate: $@";
     say "Result is $result";
}
