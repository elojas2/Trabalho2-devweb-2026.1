const BASE_URL = 'http://localhost:8080'

const JSON_HEADERS = { 'Content-Type': 'application/json' }

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

export function get<T>(path: string): Promise<T> {
  return request<T>(path);
}

export function post<T>(path: string, body: Record<string, unknown>): Promise<T> {
  return request<T>(path, {
    method: 'POST',
    headers: JSON_HEADERS,
    body: JSON.stringify(body),
  });
}

export function put<T>(path: string, body: Record<string, unknown>): Promise<T> {
  return request<T>(path, {
    method: 'PUT',
    headers: JSON_HEADERS,
    body: JSON.stringify(body),
  });
}

export function del<T>(path: string): Promise<T> {
  return request<T>(path, { method: 'DELETE' });
}
