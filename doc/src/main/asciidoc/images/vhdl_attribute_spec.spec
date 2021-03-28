stack(
 line('attribute', '/(attribute) identifier', 'of'),
 line(choice(toploop('/entity_designator', ','), 'others', 'all'), ':'),
 line('/entity_class', 'is', '/expression', ';')
)
