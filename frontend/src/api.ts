const BASE_URL = 'http://localhost:8080';

async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  const res = await fetch(`${BASE_URL}${path}`, {
    credentials: 'include',
    ...options,
  });

  const data = await res.json();

  if (!res.ok) {
    throw { status: res.status, message: data.mensagem ?? 'Erro desconhecido' };
  }

  return data as T;
}

export function get<T>(path: string) {
  return request<T>(path);
}

export function post<T>(path: string, body: Record<string, string>) {
  const params = new URLSearchParams(body);
  return request<T>(path, {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: params.toString(),
  });
}
