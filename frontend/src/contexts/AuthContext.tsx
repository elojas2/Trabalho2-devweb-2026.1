import { createContext, useContext, useState, useCallback, type ReactNode } from 'react';
import { post, get } from '../api';

interface Usuario {
  id: number;
  nome: string;
  email: string;
  admin: boolean;
}

interface AuthContextType {
  usuario: Usuario | null;
  login: (email: string, senha: string) => Promise<void>;
  logout: () => Promise<void>;
  setUsuario: (u: Usuario | null) => void;
}

const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [usuario, setUsuario] = useState<Usuario | null>(null);

  const login = useCallback(async (email: string, senha: string) => {
    const data = await post<Usuario>('/login', { email, senha });
    setUsuario(data);
  }, []);

  const logout = useCallback(async () => {
    await get('/logout');
    setUsuario(null);
  }, []);

  return (
    <AuthContext.Provider value={{ usuario, login, logout, setUsuario }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth deve ser usado dentro de AuthProvider');
  return ctx;
}
