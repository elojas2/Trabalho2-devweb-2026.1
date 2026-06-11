import { Link } from 'react-router-dom';

export interface Livro {
  id: number;
  titulo: string;
  autor: string;
  ano: number;
  disponivel: boolean;
}

interface TabelaLivrosProps {
  livros: Livro[];
  isAdmin: boolean;
  onEmprestar: (idLivro: number) => void;
}

export default function TabelaLivros({ livros, isAdmin, onEmprestar }: TabelaLivrosProps) {
  return (
    <div className="table-responsive">
      <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
        <thead>
          <tr style={{ borderBottom: '2px solid #ccc' }}>
            <th style={{ padding: '0.75rem' }}>ID</th>
            <th style={{ padding: '0.75rem' }}>Título</th>
            <th style={{ padding: '0.75rem' }}>Autor</th>
            <th style={{ padding: '0.75rem' }}>Ano</th>
            <th style={{ padding: '0.75rem' }}>Status</th>
            <th style={{ padding: '0.75rem' }}>Ações</th>
          </tr>
        </thead>
        <tbody>
          {livros.length === 0 ? (
            <tr>
              <td colSpan={6} style={{ padding: '1rem', textAlign: 'center' }}>Nenhum livro encontrado.</td>
            </tr>
          ) : (
            livros.map((livro) => (
              <tr key={livro.id} style={{ borderBottom: '1px solid #eee' }}>
                <td style={{ padding: '0.75rem' }}>{livro.id}</td>
                <td style={{ padding: '0.75rem' }}>{livro.titulo}</td>
                <td style={{ padding: '0.75rem' }}>{livro.autor}</td>
                <td style={{ padding: '0.75rem' }}>{livro.ano}</td>
                <td style={{ padding: '0.75rem' }}>
                  {livro.disponivel ? (
                    <span style={{ color: 'green', fontWeight: 'bold' }}>Disponível</span>
                  ) : (
                    <span style={{ color: 'red', fontWeight: 'bold' }}>Emprestado</span>
                  )}
                </td>
                <td style={{ padding: '0.75rem', display: 'flex', gap: '0.5rem' }}>
                  
                  {livro.disponivel && (
                    <button 
                      onClick={() => onEmprestar(livro.id)}
                      className="btn btn--primary"
                      style={{ padding: '0.3rem 0.6rem', fontSize: '0.9rem' }}
                    >
                      Emprestar
                    </button>
                  )}
                  
                  {isAdmin && (
                    <Link 
                      to={`/livros/editar/${livro.id}`} 
                      className="btn btn--edit"
                      style={{ padding: '0.3rem 0.6rem', fontSize: '0.9rem', backgroundColor: '#e2e8f0', color: 'black', textDecoration: 'none', borderRadius: '4px' }}
                    >
                      Editar
                    </Link>
                  )}
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}