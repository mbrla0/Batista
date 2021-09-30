use std::collections::{VecDeque, HashSet};

fn main() {
	let input = std::io::stdin();
	let mut input = Scanner::new(input.lock());

	let runs = input.next::<u32>();
	for _ in 0..runs { run(&mut input); }
}

fn run<R: std::io::BufRead>(input: &mut Scanner<R>) {
	let mut words = HashSet::new();
	let table = Table::from_input(input);

	for i in 0..table.edge() {
		for j in 0..table.edge() {
			probe_from(&table, j, i, &mut words);
		}
	}

	let mut words = words.into_iter().collect::<Vec<_>>();
	words.sort_by(|a, b| {
		a.len().cmp(&b.len())
			.then(a.cmp(&b))
	});
	for word in words { println!("{}", word) }
	println!()
}

fn probe_from<E>(
	table: &Table,
	x: usize,
	y: usize,
	target: &mut E)
	where E: Extend<String> {

	let mut visited = HashSet::new();
	let mut queue = VecDeque::new();

	let c = *table.get(x, y).unwrap();
	queue.push_back((x, y, String::from(c), c));
	while let Some((x, y, acc, lc)) = queue.pop_front() {
		if visited.contains(&(x, y, acc.clone(), lc)) { continue }
		visited.insert((x, y, acc.clone(), lc));

		if acc.chars().count() >= 3 {
			target.extend(std::iter::once(acc.clone()))
		}

		let mut next = |x, y| {
			let c = match table.get(x, y) {
				Some(c) => *c,
				None => return
			};
			if c <= lc { return }

			let mut next = acc.clone();
			next.push(c);

			queue.push_back((x, y, next, c))
		};

		next(x.overflowing_sub(1).0, y.overflowing_sub(1).0);
		next(x.overflowing_add(1).0, y.overflowing_sub(1).0);
		next(x.overflowing_sub(1).0, y.overflowing_add(1).0);
		next(x.overflowing_add(1).0, y.overflowing_add(1).0);

		next(x.overflowing_sub(1).0, y.overflowing_sub(0).0);
		next(x.overflowing_sub(0).0, y.overflowing_sub(1).0);
		next(x.overflowing_add(1).0, y.overflowing_add(0).0);
		next(x.overflowing_add(0).0, y.overflowing_add(1).0);
	}
}

struct Table { edge: usize, buffer: Box<[char]> }
impl Table {
	fn from_input<R: std::io::BufRead>(input: &mut Scanner<R>) -> Self {
		let edge = input.next();
		let mut buffer = vec![' '; edge * edge].into_boxed_slice();

		for i in 0..edge {
			let line = input.next::<String>();
			for (j, c) in (0..edge).into_iter().zip(line.chars()) {
				buffer[i * edge + j] = c;
			}
		}

		Self {
			edge,
			buffer
		}
	}
	fn get(&self, x: usize, y: usize) -> Option<&char> {
		if x >= self.edge { return None }
		if y >= self.edge { return None }
		self.buffer.get(y * self.edge + x)
	}
	fn edge(&self) -> usize { self.edge }
}

struct Scanner<R> { source: R, buffer: std::collections::VecDeque<String> }
impl<R: std::io::BufRead> Scanner<R> {
	fn new(source: R) -> Self {
		Self { source, buffer: Default::default() }
	}
	fn next<T: std::str::FromStr>(&mut self) -> T {
		while self.buffer.len() == 0 {
			let mut line = String::new();
			let result = std::io::BufRead::read_line(&mut self.source, &mut line);
			if result.unwrap() == 0 { break }

			line.split_whitespace()
				.map(|str| str.trim().to_owned())
				.for_each(|str| self.buffer.push_back(str));
		}
		self.buffer.pop_front()
			.and_then(|str| str.parse().ok())
			.unwrap()
	}
}
