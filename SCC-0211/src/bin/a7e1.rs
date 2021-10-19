use std::cmp::{Ordering, Reverse};
use std::collections::BinaryHeap;

fn main() {
	let input = std::io::stdin();
	let mut input = Scanner::new(input.lock());

	let (graph, pines, pine_penalty, max_time, beg, end) = {
		let nodes = input.next::<usize>();
		let edges = input.next::<usize>();
		let max_time = input.next::<u64>() * 60;
		let pine_penalty = input.next::<u64>();
		let pines = input.next::<usize>();

		let mut node_data = Vec::new();
		node_data.resize_with(nodes, Vec::new);

		let mut pine_data = Vec::new();
		pine_data.resize_with(nodes, || false);

		for _ in 0..pines {
			let index = input.next::<usize>() - 1;
			pine_data[index] = true;
		}

		for _ in 0..edges {
			let a = input.next::<usize>() - 1;
			let b = input.next::<usize>() - 1;
			let c = input.next::<u64>() * 60;

			node_data[a].push((b, c));
		}

		(node_data, pine_data, pine_penalty, max_time, 0usize, nodes - 1)
	};

	let mut tentative = Vec::new();
	tentative.resize_with(graph.len(), || u64::MAX);
	let mut queue = BinaryHeap::new();

	tentative[beg] = 0;
	queue.push(Reverse(ByDistance(Step {
		node: beg,
		dist: 0,
	})));

	while let Some(Reverse(step)) = queue.pop() {
		let distance = step.distance();
		let step = step.0;

		if distance != tentative[step.node] { continue }
		if step.node == end {
			if step.dist <= max_time {
				println!("{}", step.dist);
			} else {
				println!("-1");
			}
			return
		}

		for (neighbour, travel) in &graph[step.node] {
			let pine = if pines[*neighbour] { pine_penalty } else { 0 };
			let travel = *travel + pine;

			let alt = tentative[step.node] + travel;

			if alt < tentative[*neighbour] {
				tentative[*neighbour] = alt;
				queue.push(Reverse(ByDistance(Step {
					node: *neighbour,
					dist: alt
				})));
			}
		}
	}

	println!("-1");
}

struct Step {
	node: usize,
	dist: u64,
}
struct ByDistance(Step);
impl ByDistance {
	fn distance(&self) -> u64 { self.0.dist }
}
impl PartialEq for ByDistance {
	fn eq(&self, other: &Self) -> bool {
		self.distance().eq(&other.distance())
	}
}
impl Eq for ByDistance {}
impl PartialOrd for ByDistance {
	fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
		self.distance().partial_cmp(&other.distance())
	}
}
impl Ord for ByDistance {
	fn cmp(&self, other: &Self) -> Ordering {
		self.distance().cmp(&other.distance())
	}
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