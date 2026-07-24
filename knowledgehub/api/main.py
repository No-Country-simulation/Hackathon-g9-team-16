"""
API REST do KnowledgeHub (FastAPI) — DOIS NIVEIS.

Subir localmente:
    uvicorn api.main:app --reload

Documentacao interativa:
    http://127.0.0.1:8000/docs
"""

import sys
from pathlib import Path

from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field, field_validator

sys.path.append(str(Path(__file__).resolve().parent.parent))
from modelo import inferencia

app = FastAPI(
    title="KnowledgeHub API",
    description="Hackathon ONE - Team 16 - KnowledgeHub | Organizacao inteligente de conteudo tecnico (classificacao em dois niveis)",
    version="2.0.0",
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

class ConteudoEntrada(BaseModel):
    titulo: str = Field(..., min_length=3, max_length=300,
                        examples=["O que e machine learning?"])
    texto: str = Field(..., min_length=20, max_length=20000,
                       examples=["Os robos possuem softwares que possibilitam "
                                 "o aprendizado a partir das interacoes."])

    @field_validator("titulo", "texto")
    @classmethod
    def nao_pode_ser_so_espaco(cls, v):
        if not v.strip():
            raise ValueError("O campo nao pode conter apenas espacos em branco.")
        return v.strip()

class LoteEntrada(BaseModel):
    conteudos: list[ConteudoEntrada] = Field(..., min_length=1, max_length=100)

@app.on_event("startup")
def carregar():
    inferencia.carregar_modelo()
    print("Modelo KnowledgeHub carregado com sucesso.")

@app.get("/", tags=["Status"])
def raiz():
    return {"servico": "KnowledgeHub API", "status": "online", "documentacao": "/docs"}

@app.get("/saude", tags=["Status"])
def saude():
    try:
        art = inferencia.carregar_modelo()
        return {
            "status": "ok",
            "modelo_carregado": True,
            "areas": sorted(set(art["areas"])),
            "subareas": sorted(set(art["subareas"])),
            "tamanho_da_base": len(art["titulos"]),
        }
    except FileNotFoundError as e:
        raise HTTPException(status_code=503, detail=str(e))

@app.post("/conteudo", tags=["Processamento"])
def processar_conteudo(entrada: ConteudoEntrada):
    """Recebe um conteudo e devolve area, subarea, palavras-chave e relacionados."""
    try:
        return inferencia.processar(entrada.titulo, entrada.texto)
    except FileNotFoundError as e:
        raise HTTPException(status_code=503, detail=str(e))
    except Exception as e:
        raise HTTPException(status_code=500,
                            detail=f"Erro ao processar: {type(e).__name__}")

@app.post("/conteudo/lote", tags=["Processamento"])
def processar_lote(entrada: LoteEntrada):
    """Processa varios conteudos de uma vez (ate 100)."""
    try:
        resultados = [inferencia.processar(c.titulo, c.texto)
                      for c in entrada.conteudos]
        return {"total": len(resultados), "resultados": resultados}
    except Exception as e:
        raise HTTPException(status_code=500,
                            detail=f"Erro ao processar o lote: {type(e).__name__}")

@app.get("/taxonomia", tags=["Consulta"])
def taxonomia():
    """Lista as areas e, dentro de cada uma, as subareas conhecidas."""
    art = inferencia.carregar_modelo()
    mapa = {}
    for area, sub in zip(art["areas"], art["subareas"]):
        mapa.setdefault(area, set()).add(sub)
    return {area: sorted(subs) for area, subs in sorted(mapa.items())}
