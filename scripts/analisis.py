#!/usr/bin/env python3
"""Resume mediciones y dibuja graficas requeridas por el enunciado.

Uso: python scripts/analisis.py resultados/mediciones.csv
"""
import os
import sys
from pathlib import Path

import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
import pandas as pd

csv_path = Path(sys.argv[1] if len(sys.argv) > 1 else 'resultados/mediciones.csv')
base = csv_path.parent
fig_dir = base.parent / 'graficas'
base.mkdir(parents=True, exist_ok=True)
fig_dir.mkdir(parents=True, exist_ok=True)
raw = pd.read_csv(csv_path)
valid = raw[(raw['estado'] == 'OK') & raw['tiempo_ns'].notna()].copy()
valid['tiempo_ms'] = valid['tiempo_ns'] / 1e6
cols = ['experimento', 'algoritmo', 'tipo_entrada', 'n', 'digitos']
summary = (valid.groupby(cols, as_index=False)
           .agg(mediana_ns=('tiempo_ns', 'median'),
                mediana_ms=('tiempo_ms', 'median'),
                repeticiones=('tiempo_ns', 'count'),
                minimo_ms=('tiempo_ms','min'), maximo_ms=('tiempo_ms','max')))
summary.sort_values(['experimento', 'algoritmo', 'tipo_entrada', 'n'], inplace=True)
summary.to_csv(base/'resumen.csv', index=False)
principal = summary[summary['experimento'] == 'principal'].copy()
principal.sort_values(['algoritmo', 'tipo_entrada', 'n'], inplace=True)
principal['n_anterior'] = principal.groupby(['algoritmo','tipo_entrada'])['n'].shift()
principal['tiempo_anterior_ms'] = principal.groupby(['algoritmo','tipo_entrada'])['mediana_ms'].shift()
principal['razon_t2n_tn'] = principal['mediana_ms'] / principal['tiempo_anterior_ms']
principal.loc[principal['n'] != 2*principal['n_anterior'], 'razon_t2n_tn'] = float('nan')
principal[['algoritmo','tipo_entrada','n','n_anterior','mediana_ms','razon_t2n_tn']].to_csv(base/'razones.csv', index=False)

orden = ['Selection','Bubble','Insertion','Shell','Merge','Quick','Radix']
fig, ax = plt.subplots(figsize=(11, 7))
for alg in orden:
    d = principal[(principal['algoritmo']==alg) & (principal['tipo_entrada']=='aleatorio')]
    if not d.empty:
        ax.plot(d['n'], d['mediana_ms'], marker='o', markersize=4, label=alg)
ax.set_xscale('log', base=2)
ax.set_yscale('log')
ax.set_title('Comparacion de 7 algoritmos, entrada aleatoria (log-log)')
ax.set_xlabel('Cantidad de elementos (n), escala log2')
ax.set_ylabel('Mediana del tiempo (ms), escala logaritmica')
ax.grid(alpha=.25, which='both')
ax.legend()
fig.tight_layout()
fig.savefig(fig_dir/'comparacion_aleatoria_loglog.png', dpi=180)
plt.close(fig)
for alg in ['Insertion', 'Quick']:
    fig, ax = plt.subplots(figsize=(10, 6))
    for tipo in ['aleatorio','ascendente','descendente','duplicados']:
        d = principal[(principal['algoritmo']==alg) & (principal['tipo_entrada']==tipo)]
        if not d.empty:
            ax.plot(d['n'], d['mediana_ms'], marker='o', markersize=4, label=tipo)
    ax.set_yscale('log')
    ax.set_xscale('log', base=2)
    ax.set_xlabel('Cantidad de elementos (n), escala log2')
    ax.set_ylabel('Mediana del tiempo (ms), escala logaritmica')
    ax.set_title(alg+' Sort: tiempos por tipo de entrada')
    ax.grid(alpha=.25, which='both')
    ax.legend()
    fig.tight_layout()
    fig.savefig(fig_dir/(alg.lower()+'_tipos_entrada.png'), dpi=180)
    plt.close(fig)
radix = summary[summary['experimento'] == 'radix_digitos'].sort_values('digitos')
if not radix.empty:
    fig, ax = plt.subplots(figsize=(8, 5))
    ax.plot(radix['digitos'], radix['mediana_ms'], marker='o')
    ax.set_xticks([3, 6, 9])
    ax.set_xlabel('Digitos de los valores (d)')
    ax.set_ylabel('Mediana (ms)')
    ax.set_title('Radix Sort con n=100 000 y base 10')
    ax.grid(alpha=.25)
    fig.tight_layout()
    fig.savefig(fig_dir/'radix_digitos.png', dpi=180)
    plt.close(fig)

fails = raw[raw['estado'] != 'OK']
print(f"Series validas: {len(summary)}, mediciones OK: {len(valid)}")
print(f"Fallos/limitaciones: {len(fails)}")
if len(fails):
    print(fails[['algoritmo','tipo_entrada','n','estado']].to_string(index=False))
print('CSV resumen, razones y 4 graficas creados.')
