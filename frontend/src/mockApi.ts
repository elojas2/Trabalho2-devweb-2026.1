type Livro = { id: number; titulo: string; autor: string; ano: number; disponivel: boolean }
type Emprestimo = { id: number; livro: { id: number; titulo: string; autor: string }; dataEmprestimo: string; dataDevolucao: string | null }

let livros: Livro[] = [
  { id: 1, titulo: 'Clean Code', autor: 'Robert C. Martin', ano: 2008, disponivel: true },
  { id: 2, titulo: 'O Programador Pragmático', autor: 'Andrew Hunt', ano: 1999, disponivel: true },
  { id: 3, titulo: 'Padrões de Projeto', autor: 'Gang of Four', ano: 1994, disponivel: false },
  { id: 4, titulo: 'Introdução a Algoritmos', autor: 'Cormen et al.', ano: 2009, disponivel: true },
  { id: 5, titulo: 'You Don\'t Know JS', autor: 'Kyle Simpson', ano: 2015, disponivel: true },
]

let emprestimos: Emprestimo[] = [
  { id: 1, livro: { id: 3, titulo: 'Padrões de Projeto', autor: 'Gang of Four' }, dataEmprestimo: '2026-05-20', dataDevolucao: null },
]

let nextEmprestimoId = 2
let nextLivroId = 6

const USUARIO_DEMO = { id: 1, nome: 'Demo User', email: 'demo@biblioteca.com', admin: true }

function delay<T>(val: T): Promise<T> {
  return new Promise(resolve => setTimeout(() => resolve(val), 300))
}

export function get<T>(path: string): Promise<T> {
  if (path === '/logout') return delay(null as T)

  if (path.startsWith('/livros?')) {
    const q = new URLSearchParams(path.split('?')[1]).get('q') ?? ''
    const resultado = q
      ? livros.filter(l =>
          l.titulo.toLowerCase().includes(q.toLowerCase()) ||
          l.autor.toLowerCase().includes(q.toLowerCase())
        )
      : livros
    return delay([...resultado] as T)
  }

  const livroMatch = path.match(/^\/livros\/(\d+)$/)
  if (livroMatch) {
    const livro = livros.find(l => l.id === Number(livroMatch[1]))
    if (!livro) return Promise.reject({ message: 'Livro não encontrado.' })
    return delay({ ...livro } as T)
  }

  if (path === '/emprestimos') return delay([...emprestimos] as T)

  return Promise.reject({ message: 'Endpoint não encontrado no mock.' })
}

export function post<T>(path: string, body: Record<string, string>): Promise<T> {
  if (path === '/login') return delay(USUARIO_DEMO as T)

  if (path === '/cadastroUser') return delay(null as T)

  if (path === '/emprestimos') {
    if (body.action === 'emprestar') {
      const idLivro = Number(body.idLivro)
      const livro = livros.find(l => l.id === idLivro)
      if (!livro) return Promise.reject({ message: 'Livro não encontrado.' })
      if (!livro.disponivel) return Promise.reject({ message: 'Livro indisponível.' })
      livro.disponivel = false
      emprestimos.push({
        id: nextEmprestimoId++,
        livro: { id: livro.id, titulo: livro.titulo, autor: livro.autor },
        dataEmprestimo: new Date().toISOString().split('T')[0],
        dataDevolucao: null,
      })
      return delay(null as T)
    }
    if (body.action === 'devolver') {
      const idEmprestimo = Number(body.idEmprestimo)
      const emp = emprestimos.find(e => e.id === idEmprestimo)
      if (!emp) return Promise.reject({ message: 'Empréstimo não encontrado.' })
      emp.dataDevolucao = new Date().toISOString().split('T')[0]
      const livro = livros.find(l => l.id === emp.livro.id)
      if (livro) livro.disponivel = true
      return delay(null as T)
    }
  }

  if (path === '/livros') {
    livros.push({
      id: nextLivroId++,
      titulo: body.titulo,
      autor: body.autor,
      ano: Number(body.ano),
      disponivel: body.disponivel === 'true',
    })
    return delay(null as T)
  }

  if (path === '/livros/editar') {
    const livro = livros.find(l => l.id === Number(body.id))
    if (!livro) return Promise.reject({ message: 'Livro não encontrado.' })
    livro.titulo = body.titulo
    livro.autor = body.autor
    livro.ano = Number(body.ano)
    livro.disponivel = body.disponivel === 'true'
    return delay(null as T)
  }

  if (path === '/livros/excluir') {
    livros = livros.filter(l => l.id !== Number(body.id))
    return delay(null as T)
  }

  return Promise.reject({ message: 'Endpoint não encontrado no mock.' })
}
