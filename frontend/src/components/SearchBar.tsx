interface SearchBarProps {
  busca: string;
  setBusca: (valor: string) => void;
  onSearch: (e?: React.FormEvent) => void;
}

export default function SearchBar({ busca, setBusca, onSearch }: SearchBarProps) {
  return (
    <form onSubmit={onSearch} style={{ display: 'flex', gap: '0.5rem' }}>
      <input 
        type="text" 
        placeholder="Título ou autor..." 
        value={busca}
        onChange={(e) => setBusca(e.target.value)}
        style={{ padding: '0.5rem', borderRadius: '4px', border: '1px solid #ccc' }}
      />
      <button type="submit" className="btn btn--primary">Buscar</button>
    </form>
  );
}