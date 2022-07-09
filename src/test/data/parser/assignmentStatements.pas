program Test;
var
  curterm1 : integer;
  curterm2 : real;
  curterm3 : boolean;
  curterm4 : char;
  curterm5 : integer = 12345;
  curterm6 : real = 12345.789;
  curterm7 : boolean = true;
  curterm8 : char = 'a';
begin
  curterm1 := 1;
  curterm2 := 3.14;
  curterm5 += 1000;
  curterm6 -= 12345.789;
  curterm1 *= curterm5;
  curterm6 /= curterm2;
end.