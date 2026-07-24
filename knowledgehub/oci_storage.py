"""
Integracao com OCI Object Storage — requisito obrigatorio do desafio.

O que este script faz:
  - enviar (upload) o modelo treinado para um bucket na OCI;
  - baixar (download) o modelo do bucket, para a API usar em producao.

PRE-REQUISITOS (fazer uma vez so):
  1. pip install oci
  2. Ter o arquivo ~/.oci/config configurado.
     No console da OCI: Perfil > Minhas configuracoes > Tokens e chaves >
     Chaves de API > Adicionar chave de API. Ao criar, a OCI mostra um bloco
     de texto pronto — copie para ~/.oci/config (crie a pasta se nao existir)
     e salve a chave privada .pem no caminho indicado por 'key_file'.
  3. Criar um bucket no console: Armazenamento > Buckets > Criar bucket.

USO:
    python oci_storage.py enviar
    python oci_storage.py baixar
"""

import sys
from pathlib import Path

NOME_BUCKET = "knowledgehub-modelos"
NOME_OBJETO = "modelo_knowledgehub.joblib"
CAMINHO_LOCAL = Path(__file__).resolve().parent / "modelo" / NOME_OBJETO

def _cliente():
    """Cria o cliente autenticado da OCI e descobre o namespace da conta."""
    import oci

    config = oci.config.from_file()
    cliente = oci.object_storage.ObjectStorageClient(config)
    namespace = cliente.get_namespace().data
    return cliente, namespace

def enviar():
    """Sobe o modelo treinado para o Object Storage."""
    if not CAMINHO_LOCAL.exists():
        raise FileNotFoundError(
            f"{CAMINHO_LOCAL} nao existe. Rode 'python modelo/treinar.py' antes."
        )

    cliente, namespace = _cliente()
    tamanho_mb = CAMINHO_LOCAL.stat().st_size / 1_000_000

    with open(CAMINHO_LOCAL, "rb") as arquivo:
        cliente.put_object(
            namespace_name=namespace,
            bucket_name=NOME_BUCKET,
            object_name=NOME_OBJETO,
            put_object_body=arquivo,
        )

    print(f"Enviado: {NOME_OBJETO} ({tamanho_mb:.2f} MB)")
    print(f"Bucket: {NOME_BUCKET} | Namespace: {namespace}")

def baixar():
    """Baixa o modelo do Object Storage para a pasta modelo/."""
    cliente, namespace = _cliente()

    resposta = cliente.get_object(
        namespace_name=namespace,
        bucket_name=NOME_BUCKET,
        object_name=NOME_OBJETO,
    )

    CAMINHO_LOCAL.parent.mkdir(exist_ok=True)
    with open(CAMINHO_LOCAL, "wb") as arquivo:
        for pedaco in resposta.data.raw.stream(1024 * 1024, decode_content=False):
            arquivo.write(pedaco)

    print(f"Baixado para: {CAMINHO_LOCAL}")

def listar():
    """Lista os objetos do bucket — util para conferir se o upload funcionou."""
    cliente, namespace = _cliente()
    resposta = cliente.list_objects(namespace, NOME_BUCKET)
    if not resposta.data.objects:
        print("Bucket vazio.")
    for obj in resposta.data.objects:
        print(f"  - {obj.name}")

if __name__ == "__main__":
    acoes = {"enviar": enviar, "baixar": baixar, "listar": listar}
    acao = sys.argv[1] if len(sys.argv) > 1 else ""

    if acao not in acoes:
        print(f"Uso: python oci_storage.py [{' | '.join(acoes)}]")
        sys.exit(1)

    acoes[acao]()
